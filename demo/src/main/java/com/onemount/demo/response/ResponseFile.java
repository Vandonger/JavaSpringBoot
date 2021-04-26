package com.onemount.demo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseFile {
    private String file;
    private String link;
    private String description;

    public ResponseFile(String file, String link, String description) {
        this.file = file;
        this.link = link;
        this.description = description;
        
    }
}
