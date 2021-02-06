public interface OurInterface extends Remote { // Интерфейс должен наследоваться от Remote

    int doIt(String line) throws RemoteException;
}

public class ClassForRemote implements OurInterface { // Класс для удаленного запуска

    @Override
    public int doIt(String line) throws RemoteException {
        System.out.println(line);
        return 50;
    }
}

public class MainClass {
    private static final String UNIC_BINDING_NAME = "name1112"; // Уникальное имя для реестра

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException {
        ClassForRemote classForRemote = new ClassForRemote(); // Наш объект для удаленного запуска

        Registry registry = LocateRegistry.createRegistry(2099); // Реестр, в котором нужно регестрировать наш объект (указываем порт для обращения)
        Remote remote = UnicastRemoteObject.exportObject(classForRemote, 0); // Заглушка, которая занимается всей работой по запуску и отправе результата назад

        registry.bind(UNIC_BINDING_NAME, remote); // Регистрация на основе имени и заглушки

        Thread.sleep(Integer.MAX_VALUE); // Перевод в состояние сна, чтобы программа не завершилась
    }
}

public class Client {
    private static final String UNIC_BINDING_NAME = "name1112"; // Уникальное имя для реестра

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(2099); // Получаем существующий реестр
        OurInterface ourInterface = (OurInterface) registry.lookup(UNIC_BINDING_NAME); // Получаем наш объект

        System.out.println(ourInterface.doIt("Это строка вывелась из doIt"));
    }
}