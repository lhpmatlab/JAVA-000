public class HelloByteCode {
    /**
     * 计算第n个斐波那契数(1, 1, 2, 3, 5, 8, ...)
     * 通过for循环来实现
     * @param n
     * @return
     */
    public static int fibonacci(int n) {
        int result = 0;
        if (n <= 0) {
            return -1;
        } else if ( n == 1 || n == 2) {
            return 1;
        }
        int fib1 = 1, fib2 = 1;
        for (int i = 3; i <= n; i++) {
            result = fib1 + fib2;
            fib1 = fib2;
            fib2 = result;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(fibonacci(5));
    }
}
