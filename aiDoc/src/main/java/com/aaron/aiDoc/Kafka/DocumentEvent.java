package com.aaron.aiDoc.Kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DocumentEvent {

    private Long docJobId;
    private byte[] fileBytes;
}
