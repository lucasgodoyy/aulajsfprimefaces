package teste.junit;

import org.junit.Test;

import br.com.project.report.util.DateUtils;

import static org.junit.Assert.*;

import java.util.Calendar;

public class TesteData {

	@Test
	public void testData() {
		try {

			assertEquals("11102023", DateUtils.getDateAtualReportName());
			assertEquals("'2023-10-11'", DateUtils.formatDateSql(Calendar.getInstance().getTime()));
			assertEquals("'2023-10-11'", DateUtils.formatDateSqlSimple(Calendar.getInstance().getTime()));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

}
