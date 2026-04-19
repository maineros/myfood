import easyaccept.EasyAccept;

public class Main {
    public static void main(String[] args) {
        // passa o caminho da facade e o ficheiro de teste
        String facade = "Facade";
        
        // testes da user story 1
        EasyAccept.main(new String[] {facade, "tests/us1_1.txt"});
        EasyAccept.main(new String[] {facade, "tests/us1_2.txt"});
        
        // testes da user story 2
        EasyAccept.main(new String[] {facade, "tests/us2_1.txt"});
        EasyAccept.main(new String[] {facade, "tests/us2_2.txt"});
        
        // testes da user story 3
        EasyAccept.main(new String[] {facade, "tests/us3_1.txt"});
        EasyAccept.main(new String[] {facade, "tests/us3_2.txt"});
        
        // testes da user story 4
        EasyAccept.main(new String[] {facade, "tests/us4_1.txt"});
        EasyAccept.main(new String[] {facade, "tests/us4_2.txt"});
    }
}