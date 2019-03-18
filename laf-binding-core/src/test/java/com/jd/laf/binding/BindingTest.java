package com.jd.laf.binding;

import com.jd.laf.binding.annotation.Value;
import com.jd.laf.binding.annotation.XmlConverter;
import com.jd.laf.binding.reflect.exception.ReflectionException;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Method;
import java.util.*;

public class BindingTest {

    @Test
    public void testMapBinding() throws Exception {

        Map<String, Object> context = createContext();
        Employee employee = new Employee();
        bind(employee, context);
        Assert.assertEquals(employee.name, "name");
        Assert.assertEquals(employee.age, 27);
        Assert.assertEquals(employee.salary, 10000.00, 0);
        Assert.assertEquals(employee.weight, 63.5, 0);
        Assert.assertTrue(employee.married);
        Assert.assertEquals(employee.height, 500);
        Assert.assertNotNull(employee.birthDay);
        Assert.assertEquals(employee.company, "testss");
        Assert.assertEquals(employee.getCompany1().getName(), "name1");
        Assert.assertEquals(employee.sex, Sex.MALE);
        Assert.assertEquals(employee.getIds().size(), 3);
        Assert.assertEquals(employee.getIntIds().length, 3);
        Assert.assertEquals(employee.getMyIds().length, 3);
        Assert.assertEquals(employee.getIdSet().size(), 3);
        Assert.assertEquals(employee.getToken(), "123");
        Assert.assertEquals(employee.getIds1(), "1,2,3");

    }

    protected void bind(final Employee employee, final Map<String, Object> context) throws ReflectionException {
        Binding.bind(context, employee);
    }

    protected Map<String, Object> createContext() {
        Map<String, Object> company = new HashMap<String, Object>();
        company.put("name", "name1");
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("name", "name");
        context.put("age", 27);
        context.put("salary", 10000.00);
        context.put("weight", 63.5);
        context.put("married", "true");
        context.put("height1", 500);
        context.put("birthDay", "1970-01-01");
        context.put("company", new Company("testss"));
        context.put("company1", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><company name=\"name1\"/>");
        context.put("sex", "MALE");
        context.put("ids", Arrays.asList("1", "2", "3"));
        context.put("ids1", new String[]{"1", "2", "3"});
        context.put("myIds", "1,2,3");
        context.put("token", null);
        return context;
    }

    @Test
    public void testSetField() throws ReflectionException {
        Employee employee = new Employee();
        Binding.set(employee, "age", "68");
        Assert.assertEquals(employee.getAge(), 68);
    }

    @Test
    public void testMethodBinding() throws ReflectionException {
        Employee employee = new Employee();
        Binding.bind(new Context(), employee);
        Assert.assertEquals(employee.age, 10);
    }

    @Test
    public void testParameters() throws ReflectionException, NoSuchMethodException {

        Method method = BindingTest.class.getDeclaredMethod("annotation", int.class, int.class, int.class);

        //context已经有字段获取方法getObject
        Object[] args = Binding.bind(new Context(), this, method);
        Assert.assertArrayEquals(args, new Object[]{10, 10, 20});

    }

    @Test
    public void testGenerics() throws NoSuchMethodException, ReflectionException {
        Apple apple = new Apple();
        Method method = apple.getClass().getDeclaredMethod("test", String.class, Integer.class);
        //context已经有字段获取方法getObject
        Object[] args = Binding.bind(new Context(), apple, method);
        Assert.assertArrayEquals(args, new Object[]{"10", 10});
    }

    protected void annotation(@Value("age") int p1, @Value("age") int p2, int param2) {

    }

    public void testBindPerformance() throws ReflectionException {
        Map<String, Object> context = createContext();
        Employee employee = new Employee();
        long time = System.currentTimeMillis();
        int count = 1000000;
        for (int i = 0; i < count; i++) {
            bind(employee, context);
        }
        time = System.currentTimeMillis() - time;
        System.out.println(count * 1000 / time);
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Company {
        @XmlAttribute
        private String name;

        public Company() {
        }

        public Company(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Employee {
        @Value
        private String name;
        @Value("${age}")
        private int age;
        @Value
        private double salary;
        @Value
        private float weight;
        @Value
        private boolean married;
        @Value("height1")
        private short height = 180;
        @Value(format = "yyyy-MM-dd")
        private Date birthDay;
        @Value("company.name")
        private String company;
        @Value
        @XmlConverter
        private Company company1;
        @Value
        private Sex sex;
        @Value("ids1")
        private Set<Integer> ids;
        @Value("ids")
        private int[] intIds;
        @Value(format = ",")
        private int[] myIds;
        @Value(value = "myIds")
        private SortedSet<Integer> idSet;
        @Value(nullable = false)
        private String token = "123";
        @Value("ids1")
        private String ids1;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public double getSalary() {
            return salary;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }

        public float getWeight() {
            return weight;
        }

        public void setWeight(float weight) {
            this.weight = weight;
        }

        public boolean isMarried() {
            return married;
        }

        public void setMarried(boolean married) {
            this.married = married;
        }

        public short getHeight() {
            return height;
        }

        public void setHeight(short height) {
            this.height = height;
        }

        public Date getBirthDay() {
            return birthDay;
        }

        public void setBirthDay(Date birthDay) {
            this.birthDay = birthDay;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public Company getCompany1() {
            return company1;
        }

        public void setCompany1(Company company1) {
            this.company1 = company1;
        }

        public Sex getSex() {
            return sex;
        }

        public void setSex(Sex sex) {
            this.sex = sex;
        }

        public Set<Integer> getIds() {
            return ids;
        }

        public void setIds(Set<Integer> ids) {
            this.ids = ids;
        }

        public int[] getIntIds() {
            return intIds;
        }

        public void setIntIds(int[] intIds) {
            this.intIds = intIds;
        }

        public int[] getMyIds() {
            return myIds;
        }

        public void setMyIds(int[] myIds) {
            this.myIds = myIds;
        }

        public SortedSet<Integer> getIdSet() {
            return idSet;
        }

        public void setIdSet(SortedSet<Integer> idSet) {
            this.idSet = idSet;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getIds1() {
            return ids1;
        }

        public void setIds1(String ids1) {
            this.ids1 = ids1;
        }
    }

    public enum Sex {
        MALE,
        FEMALE
    }

    public static class Context {

        public Object getObject(String name) {
            if ("age".equals(name)) {
                return 10;
            } else if ("param2".equals(name)) {
                return 20;
            }
            return null;
        }

    }

    public interface Fruit<M, T> {

        void test(M m, T t);

    }

    public static class Apple implements Fruit<String, Integer> {

        @Override
        public void test(@Value("age") String s, @Value("age") Integer integer) {

        }
    }

}
