// przechowanie informacji o identyfikatorze kategorii oraz nazwie
// uzywana do grupowania pytan i quziow wedlug kategorii

package backend.model;

public class Kategoria {
    private int id;
    private String nazwa;

    public Kategoria() {}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    @Override
    public String toString() {
        return nazwa;
    }
}