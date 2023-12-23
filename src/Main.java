import java.util.Arrays;

interface IPolar2D {
    double getAngle();
    double abs();
}

interface IVector {
    double abs();
    double cdot(IVector param);
    double[] getComponents();
}

class Vector2D implements IVector {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public double abs() {
        return Math.sqrt(x * x + y * y);
    }
    @Override
    public double cdot(IVector param) {
        if (param instanceof Vector2D other) {
            return this.x * other.x + this.y * other.y;
        }
        return 0;
    }
    @Override
    public double[] getComponents() {
        return new double[]{x, y};
    }
}


class Polar2DAdapter implements IPolar2D, IVector {
    private final Vector2D srcVector;

    public Polar2DAdapter(Vector2D srcVector) {
        this.srcVector = srcVector;
    }

    @Override
    public double abs() {
        return srcVector.abs();
    }
    @Override
    public double getAngle() {
        double[] components = srcVector.getComponents();
        double angleRadians = Math.atan2(components[1], components[0]);
        double angleDegrees = Math.toDegrees(angleRadians);
        return angleDegrees;
    }
    @Override
    public double cdot(IVector param) {
        return srcVector.cdot(param);
    }
    @Override
    public double[] getComponents() {
        return this.srcVector.getComponents();
    }
}

class Polar2DInheritance extends Vector2D implements IVector {
    public Polar2DInheritance(double x, double y) {
        super(x, y);
    }

    public double getAngle() {
        double[] components = getComponents();
        double angleRadians = Math.atan2(components[1], components[0]);
        return Math.toDegrees(angleRadians);
    }
}

class Vector3DDecorator implements IVector {
    private final IVector srcVector;
    private final double z;

    public Vector3DDecorator(IVector srcVector, double z) {
        this.srcVector = srcVector;
        this.z = z;
    }

    @Override
    public double abs() {
        double[] srcComponents = srcVector.getComponents();
        return Math.sqrt(srcComponents[0] * srcComponents[0] + srcComponents[1] * srcComponents[1] + z * z);
    }

    @Override
    public double cdot(IVector param) {
        double[] srcComponents = srcVector.getComponents();
        double[] paramComponents = param.getComponents();

        if(paramComponents.length == 2){
            return srcComponents[0] * paramComponents[0] + srcComponents[1] * paramComponents[1];
        }
        else {
            return srcComponents[0] * paramComponents[0] + srcComponents[1] * paramComponents[1] + z * paramComponents[2];
        }
    }

    @Override
    public double[] getComponents() {
        double[] srcComponents = srcVector.getComponents();
        return new double[]{srcComponents[0], srcComponents[1], z};
    }

    public IVector cross(IVector param) {
        double[] paramComponents = param.getComponents();
        double[] srcComponents = srcVector.getComponents();

        double resultX, resultY, resultZ;
        if(paramComponents.length == 2){
            resultX = srcComponents[1] * 0 - z * paramComponents[1];
            resultY = z * paramComponents[0] - srcComponents[0] * 0;
            resultZ = srcComponents[0] * paramComponents[1] - srcComponents[1] * paramComponents[0];

        }else {
            resultX = srcComponents[1] * paramComponents[2] - z * paramComponents[1];
            resultY = z * paramComponents[0] - srcComponents[0] * paramComponents[2];
            resultZ = srcComponents[0] * paramComponents[1] - srcComponents[1] * paramComponents[0];

        }
        return new Vector3DDecorator(new Vector2D(resultX, resultY), resultZ);
    }


    public IVector getSrcV() {
        return srcVector;
    }
}
class Vector3DInheritance extends Vector2D {
    private final double z;

    public Vector3DInheritance(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }

    @Override
    public double abs() {
        double[] srcComponents = super.getComponents();
        return Math.sqrt(srcComponents[0] * srcComponents[0] + srcComponents[1] * srcComponents[1] + z * z);
    }

    @Override
    public double cdot(IVector param) {
        double[] srcComponents = super.getComponents();
        double[] paramComponents = param.getComponents();

        if(paramComponents.length == 2){
            return srcComponents[0] * paramComponents[0] + srcComponents[1] * paramComponents[1];
        }
        else {
            return srcComponents[0] * paramComponents[0] + srcComponents[1] * paramComponents[1] + z * paramComponents[2];
        }
    }

    @Override
    public double[] getComponents() {
        double[] srcComponents = super.getComponents();
        double[] components = new double[srcComponents.length + 1];
        System.arraycopy(srcComponents, 0, components, 0, srcComponents.length);
        components[srcComponents.length] = z;
        return components;
    }

    public Vector3DInheritance cross(IVector param) {
        double[] srcComponents = super.getComponents();
        double[] paramComponents = param.getComponents();

        double resultX, resultY, resultZ;
        if(paramComponents.length == 2){
            resultX = srcComponents[1] * 0 - z * paramComponents[1];
            resultY = z * paramComponents[0] - srcComponents[0] * 0;
            resultZ = srcComponents[0] * paramComponents[1] - srcComponents[1] * paramComponents[0];

        }else {
            resultX = srcComponents[1] * paramComponents[2] - z * paramComponents[1];
            resultY = z * paramComponents[0] - srcComponents[0] * paramComponents[2];
            resultZ = srcComponents[0] * paramComponents[1] - srcComponents[1] * paramComponents[0];

        }
        return new Vector3DInheritance(resultX, resultY, resultZ);
    }

    public IVector getSrcV() {
        return new Vector2D(super.getComponents()[0],super.getComponents()[1]);
    }
}

public class Main {
    public static void main(String[] args) {
        Polar2DAdapter v1 = new Polar2DAdapter(new Vector2D(2,3));
        Vector3DInheritance v2 = new Vector3DInheritance(2, 1, 3);
        Vector3DDecorator v3 = new Vector3DDecorator(new Vector2D(1,2), 4);

        System.out.println();
        System.out.println("DLA WEKTORA 1:");
        System.out.println("WSPÓŁRZĘDNE KARTEZJAŃSKIE: " + Arrays.toString(v1.getComponents()));
        System.out.println("WSPÓŁRZĘDNE BIEGUNOWE: długość = " + v1.abs() + ", kąt = " + v1.getAngle());
        System.out.println();
        System.out.println("DLA WEKTORA 2:");
        System.out.println("WSPÓŁRZĘDNE KARTEZJAŃSKIE: " + Arrays.toString(v2.getComponents()));
        System.out.println("WSPÓŁRZĘDNE BIEGUNOWE: długość = " + v2.abs() + ", kąt = " + new Polar2DInheritance(v2.getSrcV().getComponents()[0],v2.getSrcV().getComponents()[1]).getAngle());
        System.out.println();
        System.out.println("DLA WEKTORA 3:");
        System.out.println("WSPÓŁRZĘDNE KARTEZJAŃSKIE: " + Arrays.toString(v3.getComponents()));
        System.out.println("WSPÓŁRZĘDNE BIEGUNOWE: długość = " + v3.abs() + ", kąt = " + new Polar2DInheritance(v3.getSrcV().getComponents()[0],v3.getSrcV().getComponents()[1]).getAngle());
        System.out.println();
        System.out.println("ILOCZYNY SKALARNE:");
        System.out.println("W1 o W2: " + v1.cdot(v2)); //2D x 3D
        System.out.println("W3 o W3: " + v3.cdot(v3)); //3D x 3D
        System.out.println("W2 o W3: " + v2.cdot(v3)); //3D x 3D
        System.out.println();
        System.out.println("ILOCZYNY WEKTOROWE:");
        System.out.println("W1 x W2: " + Arrays.toString(new Vector3DDecorator(v2,0).cross(v3).getComponents())); //2D(zamieniony na 3D) x 3D
        System.out.println("W2 x W3: " + Arrays.toString(v2.cross(v3).getComponents()));

    }
}















