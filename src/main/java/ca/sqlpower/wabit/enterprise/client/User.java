/*
 * Copyright (c) 2009, SQL Power Group Inc.
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

package ca.sqlpower.wabit.enterprise.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import ca.sqlpower.object.SPObject;
import ca.sqlpower.wabit.AbstractWabitObject;
import ca.sqlpower.wabit.WabitObject;

public class User extends AbstractWabitObject implements UserDetails {

    private final List<Grant> grants;
    private String password;
    private GrantedAuthority[] authorities = null;
    private String fullName = null;
    private String email = null;

    public User(String username, String password) {
    	super();
        assert username != null;
        this.grants = new ArrayList<Grant>();
        this.password = password;
        super.setName(username);
    }

    protected boolean removeChildImpl(SPObject child) {
    	if (child instanceof Grant) {
    		return removeGrant((Grant) child);
    	} else {
    		return false;
    	}
    }

    public boolean allowsChildren() {
        return true;
    }

    public int childPositionOffset(Class<? extends SPObject> childType) {
    	if (Grant.class.isAssignableFrom(childType)) {
    		return 0;
    	} else {
    		throw new IllegalArgumentException("Users don't have children of type " + childType);
    	}
    }

    public List<? extends WabitObject> getChildren() {
        return this.grants;
    }

    public List<WabitObject> getDependencies() {
        return Collections.emptyList();
    }

    public void removeDependency(SPObject dependency) {
        // no-op
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String oldPassword = this.password;
        this.password = password;
        firePropertyChange("password", oldPassword, password);
    }

    public void addGrant(Grant grant) {
    	addGrant(grant, grants.size());
    }
    
    public void addGrant(Grant grant, int index) {
        this.grants.add(index, grant);
        grant.setParent(this);
        fireChildAdded(Grant.class, grant, index);
    }
    
    public boolean removeGrant(Grant grant) {
    	boolean wasRemoved = false;
        if (this.grants.contains(grant)) {
            int index = this.grants.indexOf(grant);
            wasRemoved = this.grants.remove(grant);
            grant.setParent(null);
            fireChildRemoved(Grant.class, grant, index);
        }
        return wasRemoved;
    }
    
    @Override
    protected void addChildImpl(SPObject child, int index) {
    	childPositionOffset(child.getClass());
    	addGrant((Grant) child, index);
    }
    
    public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		String oldName = this.fullName;
		this.fullName = fullName;
		firePropertyChange("fullName", oldName, this.fullName);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		String oldEmail = this.email;
		this.email = email;
		firePropertyChange("email", oldEmail, this.email);
	}
    
    /**
     * The returned list is mutable. Beware.
     */
    public List<Grant> getGrants() {
		return grants;
	}

	public GrantedAuthority[] getAuthorities() {
		if (this.authorities==null) {
			throw new RuntimeException("Programmatic error. The user manager has to fill in this user's groups before passing back to the security framework.");
		} else {
			return this.authorities;
		}
	}
	
	public void setAuthorities(GrantedAuthority[] authorities) {
		this.authorities = authorities;
	}

	public String getUsername() {
		return super.getName();
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public String toString() {
		return "Wabit User \"" + getName() + "\"";
	}

	public List<Class<? extends SPObject>> allowedChildTypes() {
		List<Class<? extends SPObject>> childTypes = new ArrayList<Class<? extends SPObject>>();
		childTypes.add(Grant.class);
		return childTypes;
	}
}
