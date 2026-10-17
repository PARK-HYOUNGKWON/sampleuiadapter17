package sample.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nexacro.uiadapter17.spring.core.data.DataSetRowTypeAccessor;
import com.nexacro17.xapi.data.DataSet;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import sample.service.DynGridService;

/**
 * 동적 그리드 서비스 구현
 * - 트랜잭션은 context-transaction.xml 의 AOP(sample..impl.*Impl.*)로 자동 적용된다.
 * - 그리드 ID는 화이트리스트(EMP, DEPT)로만 허용한다.
 *   (컬럼/테이블명을 화면에서 받은 값으로 SQL에 넣지 않기 위함 → SQL Injection 방지)
 */
@Service("dynGridService")
public class DynGridServiceImpl extends EgovAbstractServiceImpl implements DynGridService {

	private Logger logger = LoggerFactory.getLogger(DynGridServiceImpl.class);

	private static final String GRID_EMP = "EMP";
	private static final String GRID_DEPT = "DEPT";

	@Resource(name = "dynGridMapper")
	private DynGridMapper dynGridMapper;

	@Override
	public List<Map<String, Object>> selectColDef(String gridId) {
		checkGridId(gridId);
		return dynGridMapper.selectColDef(gridId);
	}

	@Override
	public List<Map<String, Object>> selectGridData(String gridId, Map<String, String> search) {
		checkGridId(gridId);
		if (search == null) {
			search = new HashMap<String, String>();
		}

		if (GRID_EMP.equals(gridId)) {
			return dynGridMapper.selectEmpList(search);
		} else if (GRID_DEPT.equals(gridId)) {
			return dynGridMapper.selectDeptList(search);
		}
		return new ArrayList<Map<String, Object>>();
	}

	@Override
	public void saveGridData(String gridId, List<Map<String, Object>> rows) {
		checkGridId(gridId);
		if (rows == null || rows.isEmpty()) {
			return;
		}

		// 삭제 → 수정 → 추가 순서로 처리 (PK 충돌 방지)
		int[] order = { DataSet.ROW_TYPE_DELETED, DataSet.ROW_TYPE_UPDATED, DataSet.ROW_TYPE_INSERTED };

		for (int rowType : order) {
			for (Map<String, Object> row : rows) {
				if (getRowType(row) != rowType) {
					continue;
				}
				logger.debug("[{}] rowType={} row={}", gridId, rowType, row);

				if (GRID_EMP.equals(gridId)) {
					if (rowType == DataSet.ROW_TYPE_INSERTED)      dynGridMapper.insertEmp(row);
					else if (rowType == DataSet.ROW_TYPE_UPDATED)  dynGridMapper.updateEmp(row);
					else                                            dynGridMapper.deleteEmp(row);
				} else if (GRID_DEPT.equals(gridId)) {
					if (rowType == DataSet.ROW_TYPE_INSERTED)      dynGridMapper.insertDept(row);
					else if (rowType == DataSet.ROW_TYPE_UPDATED)  dynGridMapper.updateDept(row);
					else                                            dynGridMapper.deleteDept(row);
				}
			}
		}
	}

	private int getRowType(Map<String, Object> row) {
		Object type = row.get(DataSetRowTypeAccessor.NAME);
		return type == null ? DataSet.ROW_TYPE_NORMAL : Integer.parseInt(String.valueOf(type));
	}

	private void checkGridId(String gridId) {
		if (!GRID_EMP.equals(gridId) && !GRID_DEPT.equals(gridId)) {
			throw new IllegalArgumentException("지원하지 않는 그리드 ID 입니다: " + gridId);
		}
	}
}
