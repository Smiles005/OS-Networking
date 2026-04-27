public class Driver {
    public static void main(String[] args) {
        // compare two strings as char arrays that are user inputted and print out where they differ
        String str1 = "hello world";
        String str2 = "hello w0rld";
        char[] arr1 = str1.toCharArray();
        char[] arr2 = str2.toCharArray();
        int length = arr1.length < arr2.length ? arr1.length : arr2.length;
        boolean leave = false;
        for (int i = 0; i < length || !leave; i++) {
            //use the compare function
            if (i < arr1.length && i < arr2.length) {

                if (Character.compare(arr1[i], arr2[i]) != 0) {
                    System.out.println("Difference at index " + i + ": " + arr1[i] + " vs " + arr2[i]);
                    leave = true;
                    break;
                }
            }else {
                System.out.println("Difference at index " + i + ": " + (i < arr1.length ? arr1[i] : "null") + " vs " + (i < arr2.length ? arr2[i] : "null"));
                leave = true;
                break;
            }

        }
    }
}
