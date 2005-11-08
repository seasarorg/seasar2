package examples.unit;

import org.seasar.extension.dataset.impl.SqlReader;
import org.seasar.extension.dataset.impl.XlsWriter;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class Db2Excel {

	private static final String PATH =
		"examples/unit/Db2Excel.dicon";
		
	public static void main(String[] args) {
		S2Container container = S2ContainerFactory.create(PATH);
		container.init();
		try {
			SqlReader reader = (SqlReader)
				container.getComponent(SqlReader.class);
			XlsWriter writer = (XlsWriter)
				container.getComponent(XlsWriter.class);
			writer.write(reader.read());
		} finally {
			container.destroy();
		}
	}
}