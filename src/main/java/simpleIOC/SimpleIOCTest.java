package simpleIOC;

public class SimpleIOCTest {
    public static void main(String[] args) throws Exception {
        String location = SimpleIOC.class.getClassLoader().getResource("spring-test.xml").getFile();
        SimpleIOC bt = new SimpleIOC(location);
        Wheel wheel = (Wheel)bt.getBean("wheel");
        System.out.println(wheel);
        Car car = (Car)bt.getBean("car");
        System.out.println(car);
    }
}
