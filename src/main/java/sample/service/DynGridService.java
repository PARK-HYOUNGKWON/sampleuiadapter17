package sample.service;

import java.util.List;
import java.util.Map;

/**
 * 동적 그리드 서비스
 * - 그리드 ID별 컬럼 정의(GRID_COL_DEF)와 데이터를 조회/저장한다.
 */
public interface DynGridService {

	/** 그리드 컬럼 정의 조회 */
	List<Map<String, Object>> selectColDef(String gridId);

	/** 그리드 데이터 조회 */
	List<Map<String, Object>> selectGridData(String gridId, Map<String, String> search);

	/** 그리드 데이터 저장 (행 상태 I/U/D 분기) */
	void saveGridData(String gridId, List<Map<String, Object>> rows);
}
