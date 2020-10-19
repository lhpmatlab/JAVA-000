package classLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) {
        try {
            Class<?> hello = new HelloClassLoader().loadClass("Hello");

            hello.getMethod("hello").invoke(hello.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = null;
        try {
            bytes = loadClassData(name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //解密字节码
        bytes = decode(bytes);

        return defineClass(name, bytes, 0, bytes.length);
    }

    /**
     * 对加密的字节码进行解密
     * @param bytes
     * @return
     */
    private byte[] decode(byte[] bytes) {

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (255 - bytes[i]);
        }

        return bytes;
    }

    /**
     * 加载字节码文件
     * @param name
     * @return
     * @throws IOException
     */
    private byte[] loadClassData(String name) throws IOException {
        File file = new File("C:\\workspace\\ge'ekJava\\L1_JVM\\src\\main\\java\\classLoader\\" + name + ".xlass");
        byte[] bytesArr = new byte[(int) file.length()];

        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArr);
        fis.close();

        return bytesArr;
    }


}
