import "./MessageBubble.css";

interface Props {
  role: "user" | "assistant";
  content: string;
  streaming?: boolean;
}

export default function MessageBubble({ role, content, streaming }: Props) {
  return (
    <div className={`bubble-row ${role}`}>
      <div className="bubble-avatar">{role === "user" ? "🧑" : "🤖"}</div>
      <div className={`bubble ${role}`}>
        {content}
        {streaming && (
          <span className="bubble-typing">
            <span /><span /><span />
          </span>
        )}
      </div>
    </div>
  );
}