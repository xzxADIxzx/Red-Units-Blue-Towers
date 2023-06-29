package rubt.annotations;

import java.lang.annotation.*;

public class Annotations {

    /** Annotation generating Icons class. */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    public @interface IconLoader {}

    /** Annotation generating methods for Send class. */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Sendable {

        /** Through which connection the packet will be transmitted. Custom will add an extra argument. */
        Con connection() default Con.custom;

        /** Which protocol will be used to deliver the packet. Reliable - TCP, unreliable - UDP. */
        boolean reliable() default true;
    }

    public enum Con {
        custom("connection"), server(""), client("rubt.net.Net.connection()");

        public final String code;

        private Con(String code) {
            this.code = code;
        }
    }
}
