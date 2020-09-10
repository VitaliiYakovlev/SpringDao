package springlecture.part3.configuration;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class HibernateCondition extends AnyNestedCondition {

    public HibernateCondition() {
        super(ConfigurationPhase.PARSE_CONFIGURATION);
    }

    @ConditionalOnProperty(name = "db.connector", havingValue = "hibernateHql")
    static class HqlCondition {
    }

    @ConditionalOnProperty(name = "db.connector", havingValue = "hibernateCriteria")
    static class CriteriaCondition {
    }
}
