import { useRef, useState, type DragEvent } from "react";
import { uploadDocument } from "../services/documentService";
import { useToast } from "../context/ToastContext";
import "./UploadBox.css";

interface Props {
  onUploaded: () => void;
}

export default function UploadBox({ onUploaded }: Props) {
  const inputRef = useRef<HTMLInputElement>(null);
  const { toast } = useToast();
  const [dragOver, setDragOver] = useState(false);
  const [progress, setProgress] = useState<number | null>(null);

  const handleFile = async (file: File) => {
    if (file.type !== "application/pdf" && !file.name.toLowerCase().endsWith(".pdf")) {
      toast("Only PDF files are allowed", "error");
      return;
    }
    try {
      setProgress(0);
      const msg = await uploadDocument(file, (p) => setProgress(p));
      toast(typeof msg === "string" ? msg : "Upload complete", "success");
      onUploaded();
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : "Upload failed";
      toast(message, "error");
    } finally {
      setProgress(null);
    }
  };

  const onDrop = (e: DragEvent<HTMLLabelElement>) => {
    e.preventDefault();
    setDragOver(false);
    const file = e.dataTransfer.files?.[0];
    if (file) handleFile(file);
  };

  return (
    <label
      className={`upload-box ${dragOver ? "drag-over" : ""}`}
      onDragOver={(e) => {
        e.preventDefault();
        setDragOver(true);
      }}
      onDragLeave={() => setDragOver(false)}
      onDrop={onDrop}
    >
      <input
        ref={inputRef}
        type="file"
        accept="application/pdf"
        hidden
        onChange={(e) => {
          const file = e.target.files?.[0];
          if (file) handleFile(file);
          if (inputRef.current) inputRef.current.value = "";
        }}
      />
      {progress !== null ? (
        <div className="upload-progress-wrap">
          <div className="upload-progress-text">Uploading… {progress}%</div>
          <div className="upload-progress-track">
            <div className="upload-progress-bar" style={{ width: `${progress}%` }} />
          </div>
        </div>
      ) : (
        <>
          <span className="upload-icon">⬆️</span>
          <span className="upload-title">Upload PDF</span>
          <span className="upload-sub">Drag & drop or click</span>
        </>
      )}
    </label>
  );
}