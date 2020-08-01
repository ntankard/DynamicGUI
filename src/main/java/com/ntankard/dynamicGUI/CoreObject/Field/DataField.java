package com.ntankard.dynamicGUI.CoreObject.Field;

import com.ntankard.dynamicGUI.CoreObject.CoreObject;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.ValueRead_DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.Filter.FieldFilter;
import com.ntankard.dynamicGUI.CoreObject.Field.Listener.FieldChangeListener;
import com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ntankard.dynamicGUI.CoreObject.Field.DataField.NewFieldState.*;

public class DataField<T> {

    // Core Data -------------------------------------------------------------------------------------------------------

    /**
     * The name of the Field. This must be unique and is used to identify and save the field
     */
    private final String identifierName;

    /**
     * The name to be displayed to the user, can be anything.
     */
    private final String displayName;

    /**
     * The data type of the field (same as T)
     */
    private final Class<T> type;

    // Field State -----------------------------------------------------------------------------------------------------

    public enum NewFieldState {
        N_BUILDING,             // Configuring the field
        N_ALL_FIELDS_FINISHED,  // All fields are finished and grouped into a container
        N_ATTACHED_TO_OBJECT,   // The field is attached to its container
        N_INITIALIZED,          // The field is fully setup and the initial value has been set
        N_ACTIVE,               // The object the field is attached to is in the database and in active use
        N_REMOVED               // The object this field is attached to has been removed, it can not be added again and should have all ties cut
    }

    /**
     * The current state of the field
     */
    private NewFieldState state = N_BUILDING;

    // Behavior Configuration Data -------------------------------------------------------------------------------------

    /**
     * A list of fields this one depends on (must be part of the same container as this one)
     */
    private final List<String> dependantFields = new ArrayList<>();

    /**
     * The fillers used to check the data
     */
    private final List<FieldFilter<T, ?>> filters = new ArrayList<>();

    /**
     * The engine to control the data
     */
    private DataCore<T> dataCore;

    /**
     * The properties to use when displaying the data
     */
    private final Display_Properties displayProperties = new Display_Properties();

    /**
     * The source of valid values that be used to set this field
     */
    private Method source;

    // Change Listeners ------------------------------------------------------------------------------------------------

    /**
     * Objects to be notified when data changes
     */
    private final List<FieldChangeListener<T>> fieldChangeListeners = new ArrayList<>();

    // Properties Of Attached Object To --------------------------------------------------------------------------------

    /**
     * The class that contains this field
     */
    private CoreObject container;

    /**
     * The type of object that contains this field
     */
    private Class<? extends CoreObject> parentType;

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param identifierName The name of the Field
     * @param type           The data type of the field (same as T)
     */
    public DataField(String identifierName, Class<T> type) {
        this.identifierName = identifierName;
        this.type = type;
        this.displayName = identifierName.replace("get", "").replace("is", "").replace("has", "");
        setDataCore(new ValueRead_DataCore<>());
    }

    /**
     * Called when all fields in a container are finished and added to the container
     *
     * @param parentType The type of object this fields belongs too
     */
    public void containerFinished(Class<? extends CoreObject> parentType) {
        if (!getState().equals(N_BUILDING))
            throw new IllegalStateException("The field has not been completed yet, or has been finished more than once");

        this.parentType = parentType;
        state = N_ALL_FIELDS_FINISHED;
    }


    /**
     * Called when this field is linked to a container
     *
     * @param container The container to link it too
     */
    public void fieldAttached(CoreObject container) {
        if (!getState().equals(N_ALL_FIELDS_FINISHED))
            throw new IllegalStateException("The field has not been added to the container yet or has already been added");

        if (!container.getClass().equals(parentType))
            throw new IllegalArgumentException("Trying to attache the field to the wrong type");

        this.container = container;
        if (getDataCore().canInitialSet()) {
            state = N_ATTACHED_TO_OBJECT;
        } else {
            state = N_INITIALIZED;
        }
    }

    /**
     * Add this field to the database along with its container object
     */
    public void add() {
        if (!getState().equals(N_INITIALIZED))
            throw new IllegalStateException("The field has not been configured, added or had its initial value set yet");
        state = N_ACTIVE;
    }

    //------------------------------------------------------------------------------------------------------------------
    //##################################################### Removal ####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Remove this field from the database
     */
    public void remove() {
        if (!getState().equals(N_ACTIVE))
            throw new IllegalStateException("The field has not been configured, added or had its initial value set yet");
        state = N_REMOVED;

        while (!getFilters().isEmpty()) {
            removeFilter(getFilters().get(0));
        }
        dataCore.detachFromField(this);
        dataCore = null;

        if (!getDependantFields().isEmpty())
            throw new IllegalStateException("Trying to remove a field that still has dependant fields attached");
    }

    /**
     * Remove a filter
     *
     * @param filter The filter to remove
     */
    private void removeFilter(FieldFilter<T, ?> filter) {
        filters.remove(filter);
        filter.detachedFromField(this);
    }

    /**
     * Remove a dependant field
     *
     * @param field The field this one depended on
     */
    public void removeDependantField(String field) {
        dependantFields.remove(field);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Field Setup ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Add a dependant field
     *
     * @param field The field this one will depend on
     */
    public void addDependantField(String field) {
        if (!getState().equals(N_BUILDING))
            throw new RuntimeException("Dependencies can only be added during setup");

        dependantFields.add(field);
    }

    /**
     * Add a new filter
     *
     * @param filter The filter to add
     */
    public void addFilter(FieldFilter<T, ?> filter) {
        if (!getState().equals(N_BUILDING))
            throw new RuntimeException("Fillers can only be added during setup");

        this.filters.add(filter);
        filter.attachedToField(this);
    }

    /**
     * Set the data core for the field. The old one is removed
     *
     * @param dataCore The field to set
     */
    public void setDataCore(DataCore<T> dataCore) {
        if (dataCore == null)
            throw new IllegalArgumentException("DataCore can not be null");

        if (!getState().equals(N_BUILDING))
            throw new IllegalStateException("DataCore can only be set during setup");

        if (!dataCore.doseSupportChangeListeners())
            if (fieldChangeListeners.size() != 0)
                throw new IllegalStateException("This data core dost not support change listeners and one has already been attached");

        if (this.dataCore != null) {
            this.dataCore.detachFromField(this);
        }
        dataCore.attachToField(this);
        this.dataCore = dataCore;
    }

    /**
     * Set the source of valid values that be used to set this field
     *
     * @param source The source of valid values that be used to set this field
     */
    public void setSource(Method source) {
        if (!getState().equals(N_BUILDING))
            throw new IllegalStateException("DataCore can only be set during setup");
        this.source = source;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Change Listener #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Add a new change listener to get called when a value changes
     *
     * @param fieldChangeListener The FieldChangeListener to add
     */
    public void addChangeListener(FieldChangeListener<T> fieldChangeListener) {
        if (!getDataCore().doseSupportChangeListeners())
            throw new IllegalStateException("The data core installed in this field dose not support change listeners");

        this.fieldChangeListeners.add(fieldChangeListener);
    }

    /**
     * Remove a change listener
     *
     * @param fieldChangeListener The FieldChangeListener to remove
     */
    public void removeChangeListener(FieldChangeListener<T> fieldChangeListener) {
        this.fieldChangeListeners.remove(fieldChangeListener);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Field Access ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the current value
     *
     * @return The current value
     */
    public T get() {
        return getDataCore().get();
    }

    /**
     * Set the initial value of the field
     *
     * @param value The value to set
     */
    public void initialSet(T value) {
        if (!getState().equals(N_ATTACHED_TO_OBJECT))
            throw new IllegalStateException("The containers has not been set of the initial value has already been provided");
        state = N_INITIALIZED;
        getDataCore().initialSet(value);
    }

    /**
     * Set the field value and perform what ever actions are required
     *
     * @param value The value to set
     */
    public void set(T value) {
        if (get() == value) {
            return;
        }
        if (!getState().equals(N_ACTIVE))
            throw new IllegalStateException("Can't set a value before the object is fully setup");

        getDataCore().set(value);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // Core Data -------------------------------------------------------------------------------------------------------

    public String getIdentifierName() {
        return identifierName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Class<T> getType() {
        return type;
    }

    // Field State -----------------------------------------------------------------------------------------------------

    public NewFieldState getState() {
        return state;
    }

    // Behavior Configuration Data -------------------------------------------------------------------------------------

    public List<String> getDependantFields() {
        return dependantFields;
    }

    public List<FieldFilter<T, ?>> getFilters() {
        return filters;
    }

    public DataCore<T> getDataCore() {
        return dataCore;
    }

    public Display_Properties getDisplayProperties() {
        return displayProperties;
    }

    public Method getSource() {
        return source;
    }

    // Change Listeners ------------------------------------------------------------------------------------------------

    public List<FieldChangeListener<T>> getFieldChangeListeners() {
        return fieldChangeListeners;
    }

    // Properties Of Attached Object To --------------------------------------------------------------------------------

    public CoreObject getContainer() {
        return container;
    }

    public Class<? extends CoreObject> getParentType() {
        return parentType;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Object Methods #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataField<?> dataField = (DataField<?>) o;
        return getIdentifierName().equals(dataField.getIdentifierName()) &&
                getType().equals(dataField.getType());
    }

    /**
     * {@inheritDoc
     */
    @Override
    public int hashCode() {
        return Objects.hash(getIdentifierName(), getType());
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        if (get() != null) {
            return identifierName + " - " + type.getSimpleName() + " - " + getState().toString() + " - " + get().toString();
        }
        return identifierName + " - " + type.getSimpleName() + " - " + getState().toString() + " - null";
    }
}
