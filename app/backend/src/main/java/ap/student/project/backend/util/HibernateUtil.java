package ap.student.project.backend.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ap.student.project.backend.entity.TestEntity;
public class HibernateUtil {
        private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
                configuration.addAnnotatedClass(TestEntity.class);
                sessionFactory = configuration.buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
