package com.jd.laf.binding;

import com.jd.laf.binding.annotation.Value;
import com.jd.laf.binding.reflect.exception.ReflectionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class BindingTest {

    @Test
    public void testBinding() throws ReflectionException {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("name", "name");
        context.put("age", 27);
        context.put("salary", 10000.00);
        context.put("weight", 63.5);
        context.put("married", "true");
        context.put("height1", 500);
        context.put("birthDay", "1970-01-01");
        context.put("company", new Company("testss"));
        context.put("sex", "MALE");
        context.put("ids", Arrays.asList("1", "2", "3"));
        Employee employee = new Employee();
        Binding.bind(context, employee);
        Assert.assertEquals(employee.name, "name");
        Assert.assertEquals(employee.age, 27);
        Assert.assertEquals(employee.salary, 10000.00, 0);
        Assert.assertEquals(employee.weight, 63.5, 0);
        Assert.assertTrue(employee.married);
        Assert.assertEquals(employee.height, 500);
        Assert.assertNotNull(employee.birthDay);
        Assert.assertEquals(employee.company, "testss");
        Assert.assertEquals(employee.sex, Sex.MALE);
        Assert.assertEquals(employee.getIds().size(), 3);

        Binding.set(employee, "age", "68");
        Assert.assertEquals(employee.getAge(), 68);
    }

    public static class Company {
        private String name;

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
        private Sex sex;
        @Value
        private Set<String> ids;

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

        public Sex getSex() {
            return sex;
        }

        public void setSex(Sex sex) {
            this.sex = sex;
        }

        public Set<String> getIds() {
            return ids;
        }

        public void setIds(Set<String> ids) {
            this.ids = ids;
        }
    }

    public enum Sex {
        MALE,
        FEMALE
    }
}
