package gha.bahaa.ibraheemfinalproject.data;

public class Motor
{
    String owner;
    String key;
    int year;
    double engine;
    String type;
    double price;
    String img;
    String phone;

    public Motor() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getEngine() {
        return engine;
    }

    public void setEngine(double engine) {
        this.engine = engine;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Motor{" +
                "owner='" + owner + '\'' +
                ", key='" + key + '\'' +
                ", year=" + year +
                ", engine=" + engine +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", img='" + img + '\'' +
                '}';
    }
}
