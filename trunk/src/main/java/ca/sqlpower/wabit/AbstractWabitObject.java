/*
 * Copyright (c) 2008, SQL Power Group Inc.
 *
 * This file is part of Wabit.
 *
 * Wabit is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Wabit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */

package ca.sqlpower.wabit;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWabitObject implements WabitObject {

    private final List<WabitChildListener> childListeners = new ArrayList<WabitChildListener>();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
    
    public void addChildListener(WabitChildListener l) {
        childListeners.add(l);
    }

    public void removeChildListener(WabitChildListener l) {
        childListeners.remove(l);
    }

    /**
     * Fires a child added event to all child listeners. The child should have
     * been added by the calling code already.
     * 
     * @param type
     *            The canonical type of the child being added
     * @param child
     *            The child object that was added
     * @param index
     *            The index of the added child within its own child list (this
     *            will be converted to the overall child position before the
     *            event object is constructed).
     */
    protected void fireChildAdded(Class<? extends WabitObject> type, WabitObject child, int index) {
        index += childPositionOffset(type);
        WabitChildEvent e = new WabitChildEvent(this, type, child, index);
        for (int i = childListeners.size() - 1; i >= 0; i++) {
            childListeners.get(i).wabitChildAdded(e);
        }
    }

    /**
     * Fires a child removed event to all child listeners. The child should have
     * been removed by the calling code.
     * 
     * @param type
     *            The canonical type of the child being removed
     * @param child
     *            The child object that was removed
     * @param index
     *            The index that the removed child was at within its own child
     *            list (this will be converted to the overall child position
     *            before the event object is constructed).
     */
    protected void fireChildRemoved(Class<? extends WabitObject> type, WabitObject child, int index) {
        index += childPositionOffset(type);
        WabitChildEvent e = new WabitChildEvent(this, type, child, index);
        for (int i = childListeners.size() - 1; i >= 0; i++) {
            childListeners.get(i).wabitChildRemoved(e);
        }
    }

    protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }
    
}
