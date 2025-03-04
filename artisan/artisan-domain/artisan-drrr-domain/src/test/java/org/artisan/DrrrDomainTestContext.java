package org.artisan;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@ActiveProfiles("local")
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public abstract class DrrrDomainTestContext{

}
