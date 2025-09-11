package org.emall.common.serialization;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.emall.common.serialization.obj.TestUser;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

class SerializationTest {

    @Test
    public void serialize() {
        List<String> address = new ArrayList<>();
        address.add("");

        List<String> logs = new ArrayList<>();
        logs.add("买了一本书");
        logs.add("买了一辆车");

        Map<String, List<TestUser.TestOrder>> orders = new HashMap<>();
        List<TestUser.TestOrder> orderList = new ArrayList<>();
        TestUser.TestOrder order = new TestUser.TestOrder();
        order.setId(1L);
        order.setOrderTime(new Date());
        order.setTotalPrice(new BigDecimal("10000.99"));
        List<TestUser.TestOrderItem> items = new ArrayList<>();
        TestUser.TestOrderItem item = new TestUser.TestOrderItem();
        item.setId(11L);
        item.setAddress("上海市宝山区");
        item.setPrice(new BigDecimal("111"));
        item.setQuantity(1);
        item.setProductId(111L);
        items.add(item);
        order.setItems(items);
        orderList.add(order);
        orders.put("202501", orderList);

        TestUser user = new TestUser();
        user.setId(1L);
        user.setName("张三");
        user.setLogs(logs);
        user.setOrders(orders);

        Object[] objects = new Object[]{null, (byte) 1, (short) 1, 1, 1L, 0.01, 0.01D, true, 'a', "abc", user};
        serialize(Serialization.serializer(SerializationType.GSON), objects);
        serialize(Serialization.serializer(SerializationType.FAST_JSON), objects);
        serialize(Serialization.serializer(SerializationType.JACKSON), objects);
    }

    private void serialize(Serializer serializer, Object... objects) {
        System.out.println("serializer = " + serializer);
        for (Object object : objects) {
            System.out.println("object = " + object);
            System.out.println("string = " + serializer.serialize(object));
        }
    }

    @Test
    public void deserialize() {
        DeserializeArgs[] args = new DeserializeArgs[]{
                new DeserializeArgs(null, null),
                new DeserializeArgs(null, Byte.class),
                new DeserializeArgs(null, Byte.TYPE),
                new DeserializeArgs(null, Short.class),
                new DeserializeArgs(null, Short.TYPE),
                new DeserializeArgs(null, Integer.class),
                new DeserializeArgs(null, Integer.TYPE),
                new DeserializeArgs(null, Long.class),
                new DeserializeArgs(null, Long.TYPE),
                new DeserializeArgs(null, Float.class),
                new DeserializeArgs(null, Float.TYPE),
                new DeserializeArgs(null, Double.class),
                new DeserializeArgs(null, Double.TYPE),
                new DeserializeArgs(null, Boolean.class),
                new DeserializeArgs(null, Boolean.TYPE),
                new DeserializeArgs(null, Character.class),
                new DeserializeArgs(null, Character.TYPE),
                new DeserializeArgs(null, String.class),
                new DeserializeArgs(null, TestUser.class),

                new DeserializeArgs("1", Byte.class),
                new DeserializeArgs("1", Byte.TYPE),
                new DeserializeArgs("1", Short.class),
                new DeserializeArgs("1", Short.TYPE),
                new DeserializeArgs("1", Integer.class),
                new DeserializeArgs("1", Integer.TYPE),
                new DeserializeArgs("1", Long.class),
                new DeserializeArgs("1", Long.TYPE),
                new DeserializeArgs("1.99", Float.class),
                new DeserializeArgs("1.99", Float.TYPE),
                new DeserializeArgs("1.99", Double.class),
                new DeserializeArgs("1.99", Double.TYPE),
                new DeserializeArgs("true", Boolean.class),
                new DeserializeArgs("true", Boolean.TYPE),
                new DeserializeArgs("c", Character.class),
                new DeserializeArgs("c", Character.TYPE),
                new DeserializeArgs("abc", String.class),
                new DeserializeArgs("{\"id\":1,\"name\":\"张三\",\"orders\":{\"202501\":[{\"id\":1,\"orderTime\":\"Sep 11, 2025, 9:42:15 AM\",\"totalPrice\":10000.99,\"items\":[{\"id\":11,\"productId\":111,\"quantity\":1,\"price\":111,\"address\":\"上海市宝山区\"}]}]},\"logs\":[\"买了一本书\",\"买了一辆车\"]}", TestUser.class),
        };
        deserialize(Serialization.deserializer(SerializationType.GSON), args);
        deserialize(Serialization.deserializer(SerializationType.JACKSON), args);
        deserialize(Serialization.deserializer(SerializationType.FAST_JSON), args);
    }

    @AllArgsConstructor
    @Data
    private static class DeserializeArgs {
        private String string;
        private Type type;
    }

    private void deserialize(Deserializer deserializer, DeserializeArgs... args) {
        System.out.println("deserializer = " + deserializer);
        for (DeserializeArgs arg : args) {
            System.out.println();
            System.out.println("string = " + arg.getString());
            System.out.println("type = " + arg.getType());
            System.out.println("object = " + deserializer.deserialize(arg.getString(), arg.getType()));
        }
    }

    @Test
    public void type() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        Class<?> clazz = list.getClass();
        System.out.println("clazz = " + clazz);

        Type type = clazz.getGenericSuperclass();
        System.out.println("type = " + type);
    }
}