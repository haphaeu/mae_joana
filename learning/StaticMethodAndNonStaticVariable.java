class StaticMethodAndNonStaticVariable {
    static int a = 0;
    int b;
    static void show_b() {
        System.out.println(b); //Error: non-static variable b cannot be referenced from a static context
    }
}
