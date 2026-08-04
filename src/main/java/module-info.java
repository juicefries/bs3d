module io.github.juicefries.bs3d {
    requires java.logging;
    requires org.joml;
    requires org.lwjgl;
    requires org.lwjgl.glfw;
    requires org.lwjgl.opengl;
    requires org.lwjgl.stb;

    exports io.github.juicefries.bs3d;
    exports io.github.juicefries.bs3d.lwjgl;
    exports io.github.juicefries.bs3d.lwjgl.glfw;

    opens io.github.juicefries.bs3d;

}