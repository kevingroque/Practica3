package com.tecsup.gestion.dao.jdbc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tecsup.gestion.dao.DepartmentDAO;
import com.tecsup.gestion.exception.DAOException;
import com.tecsup.gestion.exception.EmptyResultException;
import com.tecsup.gestion.exception.LoginException;
import com.tecsup.gestion.mapper.DepartmentMapper;
import com.tecsup.gestion.model.Department;

@Repository
public class DepartmentDAOImpl implements DepartmentDAO {

	private static final Logger logger = LoggerFactory.getLogger(DepartmentDAOImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Override
	public Department findDepartment(int department_id) throws DAOException, EmptyResultException {

		String query = "SELECT department_id, name, description, city"
				+ " FROM departments WHERE department_id = ?";

		Object[] params = new Object[] { department_id };

		try {

			Department dept = (Department) jdbcTemplate.queryForObject(query, params, new DepartmentMapper());
			//
			return dept;
			//return null;

		} catch (EmptyResultDataAccessException e) {
			throw new EmptyResultException();
		} catch (Exception e) {
			logger.info("Error: " + e.getMessage());
			throw new DAOException(e.getMessage());
		}
	}


	@Override
	public void create(String name, String description, String city) throws DAOException {

		String query = "INSERT INTO departments (name,description,city)  VALUES ( ?,?,? )";

		Object[] params = new Object[] { name, description, city };

		//Employee emp = null;
		
		try {
			// create
			jdbcTemplate.update(query, params);

		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			throw new DAOException(e.getMessage());
		}
		

	}

	@Override
	public void delete(String name) throws DAOException {

		String query = "DELETE FROM  departments WHERE name = ? ";

		Object[] params = new Object[] { name };

		try {
			jdbcTemplate.update(query, params);
		} catch (Exception e) {
			logger.info("Error: " + e.getMessage());
			throw new DAOException(e.getMessage());
		}
	}

	@Override
	public void update(String  name, String description, String city) throws DAOException {

		String query = "UPDATE departments SET description = ?, city =? WHERE name = ?";

		Object[] params = new Object[] { description, city, name };

		
		try {
			jdbcTemplate.update(query, params);
		} catch (Exception e) {
			logger.info("Error: " + e.getMessage());
			throw new DAOException(e.getMessage());
		}
	}


	@Override
	public Department findDepartmentByLogin(String name) throws DAOException, EmptyResultException {

		String query = "SELECT department_id, name, description, city"
				+ " FROM departments WHERE name = ? ";

		Object[] params = new Object[] { name };

		try {

			Department department = jdbcTemplate.queryForObject(query, params, new DepartmentMapper());
			//
			return department;

		} catch (EmptyResultDataAccessException e) {
			throw new EmptyResultException();
		} catch (Exception e) {
			logger.info("Error: " + e.getMessage());
			throw new DAOException(e.getMessage());
		}
	}
	
	@Override
	public List<Department> findAllDepartments() throws DAOException, EmptyResultException {

		String query = "SELECT department_id, name, description, city FROM departments ";

		try {

			List<Department> departments = jdbcTemplate.query(query, new DepartmentMapper());
			//
			return departments;

		} catch (EmptyResultDataAccessException e) {
			throw new EmptyResultException();
		} catch (Exception e) {
			logger.info("Error: " + e.getMessage());
			throw new DAOException(e.getMessage());
		}
	}

	@Override
	public List<Department> findDepartmentsByName(String name) throws DAOException, EmptyResultException {

		String query = "SELECT department_id, name, description, city FROM departments WHERE upper(name) like upper(?) ";

		Object[] params = new Object[] { "%" + name + "%" };

		try {

			List<Department> departments = jdbcTemplate.query(query, params, new DepartmentMapper());
			//
			return departments;

		} catch (EmptyResultDataAccessException e) {
			throw new EmptyResultException();
		} catch (Exception e) {
			logger.info("Error: " + e.getMessage());
			throw new DAOException(e.getMessage());
		}
	}


	public Department validate(String login, String pwd) throws LoginException, DAOException {
	
		logger.info("validate(): login: " + login + ", clave: " + pwd);
	
		if ("".equals(login) && "".equals(pwd)) {
			throw new LoginException("Login and password incorrect");
		}
	
		String query = "SELECT login, password, employee_id, first_name, last_name, salary, department_id  "
				+ " FROM employees WHERE login=? AND password=?";
	
		Object[] params = new Object[] { login, pwd };
	
		try {
	
			Department dept = (Department) jdbcTemplate.queryForObject(query, params, new DepartmentMapper());
			//
			return dept;
	
		} catch (EmptyResultDataAccessException e) {
			logger.info("Employee y/o clave incorrecto");
			throw new LoginException();
		} catch (Exception e) {
			logger.info("Error: " + e.getMessage());
			throw new DAOException(e.getMessage());
		}
	}

}
