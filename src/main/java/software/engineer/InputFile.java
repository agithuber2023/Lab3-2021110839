package software.engineer;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 通过命令行参数读取文件内容，并实现预处理
 */
class InputFile
{
    private static final String FILE_PATH = "article.txt";
    private String file_path = null;
    private final String[] words;

    public InputFile(String[] args) throws IOException {
        read_args(args);
        this.words = read();
    }

    /**
     * 读取命令行参数中的指定文件（-f, --file）
     * @param args 参数列表
     */
    public void read_args(String[] args){
        for (int i = 0; i < args.length; i++) {
            if (("-f".equals(args[i]) || "--file".equals(args[i])) && i + 1 < args.length) {
                this.file_path = args[i + 1];
                break;
            }
        }
        if (this.file_path == null) this.file_path = FILE_PATH;
    }

    /**
     * 输入文件，预处理文件内容，返回单词列表
     * @return 单词列表
     */
    private String[] read() throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(this.file_path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }
        }
        String filter_non_alphabet = content.toString().replaceAll("[^A-Za-z]", " "); // 将非字母字符替换为空格
        return filter_non_alphabet.toLowerCase().split("\\s+");
    }

    public String[] getWords() {
        return this.words;
    }
}
