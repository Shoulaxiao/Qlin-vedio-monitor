
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CodeGenerator {

    private String driverClass;
    private String url;
    private String username;
    private String password;

    private static String scanner() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入表名，多个以英文逗号分隔：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的表名信息！");
    }

    public static void main(String[] args) {
        String tableNamesStr = scanner();
        if (StringUtils.isEmpty(tableNamesStr)) {
            throw new MybatisPlusException("输入的表名不能为空！");
        }
        String[] tableNames = tableNamesStr.split(",");

        AutoGenerator autoGenerator = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        //生成的时间类型使用java.util.date
        gc.setDateType(DateType.ONLY_DATE);

        // 得到当前项目的路径
        String oPath = System.getProperty("user.dir");
        // 生成文件输出根目录
        gc.setOutputDir(oPath + "/src/main/java");
        // 生成完成后不弹出文件框
        gc.setOpen(false);
        // 文件覆盖
        gc.setFileOverride(true);
        gc.setActiveRecord(false);
        gc.setEnableCache(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        gc.setAuthor("shoulaxiao");
        gc.setIdType(IdType.AUTO);

        autoGenerator.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");

        //业务库账号
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setUrl("jdbc:mysql://localhost:3306/videoDB?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=utf8");
//        dsc.setUrl("mysql://127.0.0.1:3306/videoDB?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&useSSL=false&serverTimezone=GMT%2B8");

        autoGenerator.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setInclude(tableNames);
        autoGenerator.setStrategy(strategy);

        // 模板配置
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController(StringUtils.EMPTY);
        //关闭默认xml生成,调整到resource目录下
        templateConfig.setXml(null);
        autoGenerator.setTemplate(templateConfig);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.qinglin.qlinvediomonitor.mp");
        pc.setMapper("mapper");
        pc.setEntity("entity");
        pc.setXml("xml");
        autoGenerator.setPackageInfo(pc);

        // 自定义配置:将xml生成至resource目录下
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return oPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        autoGenerator.setCfg(cfg);

        // 执行生成
        autoGenerator.execute();
    }
}
