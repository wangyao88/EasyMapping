EasyMapping
    实体属性映射器。本版本为静态扫描加反射实现静态映射

如何使用
enableLog 是否开启日志
MappingStructStarterBuilder.newBuilder().basePackages(basePackage1, basePackage2, ...).build().start();

public static void main(String[] args) throws ScanException {
    Person person = new Person();
    person.setId("person-id");
    person.setPersonName("person-name");
    person.setAge(25);
    person.setHeight(170);
    Address address = new Address();
    address.setRoad("road");
    person.setAddress(address);

    MappingStructStarter.MappingStructStarterBuilder.newBuilder().enableLog().build().start();

    Optional<Student> optional = MappingStructManager.mapping(Student.class, person);
    optional.ifPresent(student -> {
        System.out.println(student.getId());
        System.out.println(student.getName());
        System.out.println(student.getAge());
        System.out.println(student.getHeight());
        System.out.println(student.getDate());
        System.out.println(student.getRoadName());
    });

    System.out.println("---------------------------");
    Student student = new Student();
    student.setId("student");
    MappingStructManager.mapping(student, person);
    System.out.println(student.getId());
    System.out.println(student.getName());
    System.out.println(student.getAge());
    System.out.println(student.getHeight());
    System.out.println(student.getDate());
    System.out.println(student.getRoadName());
}