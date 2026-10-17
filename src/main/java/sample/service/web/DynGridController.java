package sample.service.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexacro.uiadapter17.spring.core.annotation.ParamDataSet;
import com.nexacro.uiadapter17.spring.core.annotation.ParamVariable;
import com.nexacro.uiadapter17.spring.core.data.NexacroResult;

import sample.service.DynGridService;

/**
 * 동적 그리드 Controller
 *
 * 넥사크로 호출 예)
 *   조회 : transaction("search", "svc::selectDynGrid.do", "ds_search=ds_search", "ds_colDef=ds_colDef ds_data=ds_data", "gridId=EMP", ...)
 *   저장 : transaction("save",   "svc::saveDynGrid.do",   "ds_data=ds_data:U",   "",                                  "gridId=EMP", ...)
 */
@Controller
public class DynGridController {

	private Logger logger = LoggerFactory.getLogger(DynGridController.class);

	@Resource(name = "dynGridService")
	private DynGridService dynGridService;

	/** 컬럼 정의 + 데이터를 한 번에 조회 */
	@RequestMapping(value = "/selectDynGrid.do")
	public NexacroResult selectDynGrid(
			@ParamVariable(name = "gridId") String gridId,
			@ParamDataSet(name = "ds_search", required = false) Map<String, String> search) {

		logger.debug("gridId={}, search={}", gridId, search);

		NexacroResult result = new NexacroResult();
		result.addDataSet("ds_colDef", dynGridService.selectColDef(gridId));
		result.addDataSet("ds_data", dynGridService.selectGridData(gridId, search));
		return result;
	}

	/** 컬럼 정의만 조회 (그리드 틀만 먼저 그릴 때) */
	@RequestMapping(value = "/selectDynGridColDef.do")
	public NexacroResult selectDynGridColDef(@ParamVariable(name = "gridId") String gridId) {
		NexacroResult result = new NexacroResult();
		result.addDataSet("ds_colDef", dynGridService.selectColDef(gridId));
		return result;
	}

	/** 저장 (I/U/D) */
	@RequestMapping(value = "/saveDynGrid.do")
	public NexacroResult saveDynGrid(
			@ParamVariable(name = "gridId") String gridId,
			@ParamDataSet(name = "ds_data") List<Map<String, Object>> rows) {

		dynGridService.saveGridData(gridId, rows);
		return new NexacroResult();
	}
}
