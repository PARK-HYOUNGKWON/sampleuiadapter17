package sample.service.impl;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("dynGridMapper")
public interface DynGridMapper {

	/* 컬럼 정의 */
	List<Map<String, Object>> selectColDef(String gridId);

	/* 사원(EMP) */
	List<Map<String, Object>> selectEmpList(Map<String, String> search);
	void insertEmp(Map<String, Object> row);
	void updateEmp(Map<String, Object> row);
	void deleteEmp(Map<String, Object> row);

	/* 부서(DEPT) */
	List<Map<String, Object>> selectDeptList(Map<String, String> search);
	void insertDept(Map<String, Object> row);
	void updateDept(Map<String, Object> row);
	void deleteDept(Map<String, Object> row);
}
