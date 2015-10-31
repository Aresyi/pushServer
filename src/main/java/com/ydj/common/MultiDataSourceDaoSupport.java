package com.ydj.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.support.DaoSupport;
import org.springframework.jdbc.core.JdbcTemplate;

/**
* 
* @author : Ares.yi
* @createTime : 2013-11-10 上午11:13:42 
* @version : 1.0 
* @description : 多数据源
*
 */
public abstract class MultiDataSourceDaoSupport extends DaoSupport {

	private String defaultAlilas;

	private JdbcTemplate defultJdbcTemplate;

	private final Map<String, DataSource> dataSources = new HashMap<String, DataSource>(4);

	public void setDataSources(Map<String, DataSource> dataSources) {
		if (dataSources != null) {
			this.dataSources.putAll(dataSources);
		}
	}
	
	public Map<String, DataSource> getDataSources() {
		return Collections.unmodifiableMap(this.dataSources);
	}

	public final void setDefault(String alias) {
		if (this.defaultAlilas == null || this.defultJdbcTemplate == null) {
			this.defaultAlilas = alias;
			this.defultJdbcTemplate = new JdbcTemplate(
					this.dataSources.get(alias));
		}
	}
	
	public final JdbcTemplate getJdbcTemplate() {
		if (null == this.defaultAlilas)
			throw new IllegalArgumentException("'default' connont be null");

		return null == this.defultJdbcTemplate ? new JdbcTemplate(
				this.dataSources.get(defaultAlilas)) : this.defultJdbcTemplate;
	}
	
	public final JdbcTemplate getJdbcTemplate(DB db) {
		String alias = db.alias;
		if (null == alias)
			throw new IllegalArgumentException("'alias' connont be null");

		return alias.equals(this.defaultAlilas) ? this.defultJdbcTemplate
				: new JdbcTemplate(this.dataSources.get(alias));
	}

	@Override
	protected void checkDaoConfig() {
		if (this.dataSources == null || this.dataSources.isEmpty()) {
			throw new IllegalArgumentException(
					"'dataSource' or 'jdbcTemplate' is required");
		}
	}

}
