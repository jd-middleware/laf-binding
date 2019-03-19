package com.jd.laf.binding.marshaller;

import com.jd.laf.binding.reflect.TypeReference;
import org.junit.Assert;
import org.junit.Test;

public class FastJsonTest {

    @Test
    public void testTypeReference() throws Exception {

        Marshaller marshaller = new FastJsonProvider.FastJsonMarshaller();
        Unmarshaller unmarshaller = new FastJsonProvider.FastJsonUnmarshaller();

        QPageQuery<QUser> qPageQuery = new QPageQuery<QUser>(1, 10, new QUser("he"));
        String value = marshaller.marshall(qPageQuery);
        qPageQuery = unmarshaller.unmarshall(value, new TypeReference<QPageQuery<QUser>>(QUser.class) {

        });
        Assert.assertNotNull(qPageQuery);

    }

    public static class QPageQuery<M> {

        protected int page;
        protected int size;
        protected M query;

        public QPageQuery() {
        }

        public QPageQuery(int page, int size, M query) {
            this.page = page;
            this.size = size;
            this.query = query;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public M getQuery() {
            return query;
        }

        public void setQuery(M query) {
            this.query = query;
        }
    }

    public static class QUser {

        protected String name;

        public QUser() {
        }

        public QUser(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
