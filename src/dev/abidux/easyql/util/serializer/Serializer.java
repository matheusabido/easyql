package dev.abidux.easyql.util.serializer;

public class Serializer<F, T> {

    public final String sqlType;
    public final Encoder<F, T> encoder;
    public final Decoder<F, T> decoder;
    public Serializer(String sqlType, Encoder<F, T> encoder, Decoder<F, T> decoder) {
        this.sqlType = sqlType;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public interface Encoder<F, T> {
        T encode(F object);
    }
    public interface Decoder<F, T> {
        F decode(T encodedObject);
    }

}