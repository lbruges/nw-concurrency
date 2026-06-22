package com.codigofacilito.common.props.model.req;

import com.codigofacilito.common.props.model.PropsWithPrefix;

import java.util.Optional;
import java.util.Properties;

import static com.codigofacilito.common.props.util.PropsUtils.PROPS_SEPARATOR;
import static com.codigofacilito.common.props.util.PropsUtils.readStringProperty;

public class WebRequestProperties implements PropsWithPrefix {

    private final String url;
    private final String seqAId;
    private final String seqBId;
    public static final String PROPS_PREFIX = "req";

    private static final WebRequestProperties DEFAULT = new WebRequestProperties("https://rest.ensembl.org/sequence/id/%s?type=cdna;content-type=application/json",
            "ENSG00000239615", "ENSG00000239617");

    public WebRequestProperties(String url, String seqAId, String seqBId) {
        this.url = url;
        this.seqAId = seqAId;
        this.seqBId = seqBId;
    }

    public WebRequestProperties(ReqPropsBuilder builder) {
        this.url = builder.url;
        this.seqAId = builder.seqAId;
        this.seqBId = builder.seqBId;
    }

    public static ReqPropsBuilder builder() {
        return new ReqPropsBuilder();
    }

    @Override
    public String toString() {
        return "WebRequestProperties{" +
                "url='" + url + '\'' +
                ", seqAId='" + seqAId + '\'' +
                ", seqBId='" + seqBId + '\'' +
                '}';
    }

    @Override
    public String getPrefix() {
        return PROPS_PREFIX;
    }

    public String getUrl() {
        return url;
    }

    public String getSeqAId() {
        return seqAId;
    }

    public String getSeqBId() {
        return seqBId;
    }

    public static class ReqPropsBuilder {
        private Optional<Properties> propertiesOpt;
        private String url;
        private String seqAId;
        private String seqBId;

        public ReqPropsBuilder fromProperties(Optional<Properties> propertiesOpt) {
            this.propertiesOpt = propertiesOpt;
            return this;
        }

        public WebRequestProperties build() {
            propertiesOpt.ifPresent(properties -> {
                url = readStringProperty(properties, String.join(PROPS_SEPARATOR, "url"), DEFAULT.url);
                seqAId = readStringProperty(properties, String.join(PROPS_SEPARATOR, "seq-a-id"), DEFAULT.seqAId);
                seqBId = readStringProperty(properties, String.join(PROPS_SEPARATOR, "seq-b-id"), DEFAULT.seqBId);
            });

            return new WebRequestProperties(this);
        }
    }

}
