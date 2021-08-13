package today.Sapphire.start;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Value<T> {
    public static final List<Value> list;

    static {
        list = new ArrayList<Value>();
    }

    private final T defaultValue;
    private final String name;
    public T value;
    public boolean isValueBoolean;
    public boolean isValueInteger;
    public boolean isValueFloat;
    public boolean isValueDouble;
    public boolean isValueMode;
    public boolean isValueLong;
    public boolean isValueByte;
    public ArrayList<String> mode;
    public boolean set;
    public float currentRadius;
    private T valueMin;
    private T valueMax;
    private double step;
    private int current;
    private String modeTitle;


    public Value(final String classname, final String modeTitle, final String[] value,final int current) {
        resetValue();
        this.defaultValue = this.value;
        this.isValueMode = true;
        this.step = 0.1;
        this.mode = new ArrayList<>();
        for(int i =0; i < value.length; i++) {
            this.mode.add(value[i]);
        }
        this.current = current;
        this.name = classname + "_" + "Mode";
        this.modeTitle = modeTitle;
        Value.list.add(this);
    }

    public Value(final String name, final String mode, final T value, final T valueMin, final T valueMax, final double steps) {
        resetValue();
        this.defaultValue = value;
        this.name = name + "_" + mode;
        this.value = value;
        this.valueMin = valueMin;
        this.valueMax = valueMax;
        this.step = steps;
        if (value instanceof Double) {
            this.isValueDouble = true;
        }
        if (value instanceof Integer) {
            this.isValueInteger = true;
        }
        if (value instanceof Long) {
            this.isValueLong = true;
        }
        if (value instanceof Byte) {
            this.isValueByte = true;
        }
        Value.list.add(this);
    }


    public Value(final Class<?> classname, final String mode, final T value) {
        resetValue();
        this.defaultValue = value;
        this.name = classname.getSimpleName() + "_" + mode;
        this.value = value;
        if (value instanceof Boolean) {
            this.isValueBoolean = true;
        } else if (value instanceof Integer) {
            this.isValueInteger = true;
        } else if (value instanceof Float) {
            this.isValueFloat = true;
        } else if (value instanceof Long) {
            this.isValueLong = true;
        } else if (value instanceof Byte) {
            this.isValueByte = true;
        }
        Value.list.add(this);
    }


    public Value(final String name, final String mode, final T value) {
        resetValue();
        this.defaultValue = value;
        this.name = name + "_" + mode;
        this.value = value;
        if (value instanceof Boolean) {
            this.isValueBoolean = true;
        } else if (value instanceof Integer) {
            this.isValueInteger = true;
        } else if (value instanceof Float) {
            this.isValueFloat = true;
        } else if (value instanceof Long) {
            this.isValueLong = true;
        } else if (value instanceof Byte) {
            this.isValueByte = true;
        }
        Value.list.add(this);
    }

    public void resetValue() {
        this.set = false;
        this.currentRadius = 4.0f;
        this.isValueBoolean = false;
        this.isValueInteger = false;
        this.isValueFloat = false;
        this.isValueDouble = false;
        this.isValueLong = false;
        this.isValueByte = false;
    }

    public static Value getDoubleValueByName(final String name) {
        for (final Value value : Value.list) {
            if (value.isValueDouble && value.getValueName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    public static Value getModeValue(final String valueName, final String title) {
        for (final Value value : Value.list) {
            if (value.isValueMode && value.getValueName().equalsIgnoreCase(valueName) && value.getModeTitle().equalsIgnoreCase(title)) {
                return value;
            }
        }
        return null;
    }

    public static List<Value> getValue(Mod currentModule) {
        ArrayList<Value> list = new ArrayList<Value>();
        for (Value value : Value.list) {
            if (value.name.split("_")[0].equalsIgnoreCase(currentModule.getName())) {
                list.add(value);
            }
        }
        return list;
    }

    public Value<T> LoadValue(final String[] Value) {
        this.mode.addAll(Arrays.asList(Value));
        return this;
    }

    public int getCurrentMode() {
        if (current > this.mode.size() - 1) {
            System.out.println("Value is to big! Set to 0. (" + this.mode.size() + ")");
            this.current = 0;
        }
        return this.current;
    }

    public void setCurrentMode(final int current) {
        if (current > this.mode.size() - 1) {
            System.out.println("Value is to big! Set to 0. (" + this.mode.size() + ")");
            this.current = 0;
            return;
        }
        this.current = current;
    }

    public ArrayList<String> listModes() {
        return this.mode;
    }

    public String getModeTitle() {
        return this.modeTitle;
    }

    public String getModeAt(final int index) {
        return this.mode.get(index);
    }

    public String getModeAt(final String modeName) {
        for (int i = 0; i < this.mode.size(); ++i) {
            if (this.mode.get(i).equalsIgnoreCase(modeName)) {
                return this.mode.get(i);
            }
        }
        return "NULL";
    }

    public int getModeInt(final String modeName) {
        for (int i = 0; i < this.mode.size(); ++i) {
            if (this.mode.get(i).equalsIgnoreCase(modeName)) {
                return i;
            }
        }
        return 0;
    }

    public boolean isCurrentMode(final String modeName) {
        return this.getModeAt(this.getCurrentMode()).equalsIgnoreCase(modeName);
    }

    public boolean getValueName(final String modeName) {
        return this.getModeAt(this.getCurrentMode()).equalsIgnoreCase(modeName);
    }

    public String getAllModes() {
        String all = "";
        for (int i = 0; i < this.mode.size(); ++i) {
            all = all + this.mode.get(i);
        }
        return all;
    }

    public final String getValueName() {
        return this.name;
    }

    public String getDisplayTitle() {
        if (this.isValueMode) {
            return this.getModeTitle();
        }
        return this.getValueName().split("_")[1];
    }

    public final T getValueMin() {
        if (this.value instanceof Double) {
            return this.valueMin;
        }
        return null;
    }

    public final double getSteps() {
        return this.step;
    }

    public final T getValueMax() {
        if (this.value instanceof Double) {
            return this.valueMax;
        }
        return null;
    }

    public final T getDefaultValue() {
        return this.defaultValue;
    }

    public final T getValueState() {
        return this.value;
    }

    public final void setValueState(final T value) {
        this.value = value;
    }

    public final T getValue() {
        return this.value;
    }

    public String[] getModes() {
        return mode.toArray(new String[0]);
    }

    public String getName() {
        return name.split("_")[1];
    }

    public final String getModeName() {
        return String.valueOf(this.getModeAt(this.getCurrentMode()));
    }

}
