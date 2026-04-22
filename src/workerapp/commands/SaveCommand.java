package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import workerapp.io.FileManager;
import java.util.HashMap;

public class SaveCommand extends Command {
    private String fileName;

    public SaveCommand(HashMap<String, Worker> collection, String fileName) {
        super("save", "сохранить коллекцию в файл", collection);
        this.fileName = fileName;
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("❌ Имя файла не указано");
            return;
        }
        FileManager.saveToFile(fileName, collection);
        System.out.println("💾 Сохранено в " + fileName);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}