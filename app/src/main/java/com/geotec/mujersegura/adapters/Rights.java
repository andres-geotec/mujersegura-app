package com.geotec.mujersegura.adapters;

/**
 * Created by andres on 09/10/2019.
 *
 * Reprresentación de tabla:
 * Atenciones(cve -> key, name, description, source, download);
 */
public class Rights {
    private long id;
    private String cve;
    private String name;
    private String description;
    private String source;
    private String timestamp;
    private boolean download;

    public Rights() {}

    public Rights(long id, String cve, String name, String description, String source, boolean download) {
        this.id = id;
        this.cve = cve;
        this.name = name;
        this.description = description;
        this.source = source;
        this.download = download;
    }

    public Rights(long id, String cve, String name, String description, String source, String timestamp, boolean download) {
        this.id = id;
        this.cve = cve;
        this.name = name;
        this.description = description;
        this.source = source;
        this.timestamp = timestamp;
        this.download = download;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }
}
