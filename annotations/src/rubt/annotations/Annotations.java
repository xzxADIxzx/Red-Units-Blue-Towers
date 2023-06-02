package rubt.annotations;

import java.lang.annotation.*;

public class Annotations {

    /** Annotation generating constructors for packets. */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Sendable {

        /** Through which connection the packet will be transmitted. Custom will add an extra argument. */
        Con connection() default Con.custom;

        /** Which protocol will be used to deliver the packet. Reliable - TCP, unreliable - UDP. */
        boolean reliable() default true;
    }

    public enum Con {
        custom, server, client;
    }
}
