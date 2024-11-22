package pl.krywion.usosremastered;

import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
    protected static final String TEST_EMAIL = "test123@example.com";
    protected static final String TEST_PASSWORD = "test123";
    protected static final String TEST_FIRST_NAME = "John";
    protected static final String TEST_LAST_NAME = "Doe";
    protected static final String TEST_PESEL = "83103175464";
    protected static final Long TEST_ALBUM_NUMBER = 1L;
    protected static final Long TEST_DEPARTMENT_ID = 1L;

}
