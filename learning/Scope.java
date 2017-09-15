class Scope {
    public static void main(String[] args) {
        int x = 5;
        if (x==5) {
            int x = 6;
            System.out.println("x="+x);
        }
        System.out.println("x="+x);
    }
}

                