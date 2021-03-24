package kr.or.ddit.batch.yogurt;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import java.util.List;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

public class CustomMybatisBatchItemWriter<T> implements ItemWriter<List<T>>, InitializingBean {
			 	

	  private static final Logger LOGGER = LoggerFactory.getLogger(CustomMybatisBatchItemWriter.class);

	  private SqlSessionTemplate sqlSessionTemplate;

	  private String statementId;

	  private boolean assertUpdates = true;

	  private Converter<T, ?> itemToParameterConverter = new PassThroughConverter<>();

	  /**
	   * Public setter for the flag that determines whether an assertion is made that number of BatchResult objects returned
	   * is one and all items cause at least one row to be updated.
	   *
	   * @param assertUpdates
	   *          the flag to set. Defaults to true;
	   */
	  public void setAssertUpdates(boolean assertUpdates) {
	    this.assertUpdates = assertUpdates;
	  }

	  /**
	   * Public setter for {@link SqlSessionFactory} for injection purposes.
	   *
	   * @param sqlSessionFactory
	   *          a factory object for the {@link SqlSession}.
	   */
	  public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
	    if (sqlSessionTemplate == null) {
	      this.sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
	    }
	  }

	  /**
	   * Public setter for the {@link SqlSessionTemplate}.
	   *
	   * @param sqlSessionTemplate
	   *          a template object for use the {@link SqlSession} on the Spring managed transaction
	   */
	  public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
	    this.sqlSessionTemplate = sqlSessionTemplate;
	  }

	  /**
	   * Public setter for the statement id identifying the statement in the SqlMap configuration file.
	   *
	   * @param statementId
	   *          the id for the statement
	   */
	  public void setStatementId(String statementId) {
	    this.statementId = statementId;
	  }

	  /**
	   * Public setter for a converter that converting item to parameter object.
	   * <p>
	   * By default implementation, an item does not convert.
	   *
	   * @param itemToParameterConverter
	   *          a converter that converting item to parameter object
	   * @since 2.0.0
	   */
	  public void setItemToParameterConverter(Converter<T, ?> itemToParameterConverter) {
	    this.itemToParameterConverter = itemToParameterConverter;
	  }

	  /**
	   * Check mandatory properties - there must be an SqlSession and a statementId.
	   */
	  @Override
	  public void afterPropertiesSet() {
	    notNull(sqlSessionTemplate, "A SqlSessionFactory or a SqlSessionTemplate is required.");
	    isTrue(ExecutorType.BATCH == sqlSessionTemplate.getExecutorType(),
	        "SqlSessionTemplate's executor type must be BATCH");
	    notNull(statementId, "A statementId is required.");
	    notNull(itemToParameterConverter, "A itemToParameterConverter is required.");
	  }

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void write(final List<? extends List<T>> items) {

	    if (!items.isEmpty()) {
	      LOGGER.debug(() -> "Executing batch with " + items.size() + " items.");

	      for (List<T> list : items) {
	    	  for(T item : list) {
	    		  sqlSessionTemplate.update(statementId, itemToParameterConverter.convert(item));
	    	  }
	      }

	      List<BatchResult> results = sqlSessionTemplate.flushStatements();

	      if (assertUpdates) {
	        if (results.size() <= 0) {
	          throw new InvalidDataAccessResourceUsageException("Batch execution returned invalid results. "
	              + "Expected 1 but number of BatchResult objects returned was " + results.size());
	        }

	        int[] updateCounts = results.get(0).getUpdateCounts();

	        for (int i = 0; i < updateCounts.length; i++) {
	          int value = updateCounts[i];
	          if (value == 0) {
	            throw new EmptyResultDataAccessException(
	                "Item " + i + " of " + updateCounts.length + " did not update any rows: [" + items.get(i) + "]", 1);
	          }
	        }
	      }
	    }
	  }

	  private static class PassThroughConverter<T> implements Converter<T, T> {

	    @Override
	    public T convert(T source) {
	      return source;
	    }

	  }

	}
