package com.tlabs.android.jeeves.views.skills;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.eve.api.character.Certificate;
import com.tlabs.eve.api.character.Certificate.Level;
import com.tlabs.eve.api.character.CertificateTree;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class CertificateTreeAdapter extends BaseExpandableListAdapter {

    private static final class TreeGroupHolder {
        private static final int LAYOUT = R.layout.row_certificates_expanded;
        
        private final TextView groupNameView;
        private final ImageView groupLevelView;
        
        private TreeGroupHolder(final View view) {
            this.groupNameView = (TextView)view.findViewById(R.id.certificateGroupText);
            this.groupLevelView = (ImageView)view.findViewById(R.id.certificateGroupImage);
        }
        
        private void render(final String groupName) {
            this.groupLevelView.setVisibility(View.GONE);
            this.groupNameView.setText(groupName);            
        }
        
        private void render(final String groupName, final Level highestLevel) {
            
            this.groupNameView.setText(groupName);            
            if (null == highestLevel) {
                this.groupLevelView.setVisibility(View.GONE);
                return;
            }
            
            this.groupLevelView.setVisibility(View.VISIBLE);
            switch (highestLevel) {
                case BASIC:
                    this.groupLevelView.setImageLevel(1);
                    break;
                case STANDARD:
                    this.groupLevelView.setImageLevel(2);
                    break;
                case IMPROVED:
                    this.groupLevelView.setImageLevel(3);
                    break;
                case ADVANCED:
                    this.groupLevelView.setImageLevel(4);
                    break;
                case ELITE:
                    this.groupLevelView.setImageLevel(5);
                    break;
                default:
                    this.groupLevelView.setImageLevel(0);
                    break;
            }           
        }
        
        public static View create(final Context context) {
            View view = LayoutInflater.from(context).inflate(LAYOUT, null);
            view.setTag(new TreeGroupHolder(view));
            return view;
        }
        
        public static void render(final View view, final String groupName) {
            TreeGroupHolder h = (TreeGroupHolder)view.getTag();
            h.render(groupName);
        }
        
        public static void render(final View view, final String groupName, final Level highestLevel) {
            TreeGroupHolder h = (TreeGroupHolder)view.getTag();
            h.render(groupName, highestLevel);
        }
    }

    private static final class TreeChildHolder {
        private static final int LAYOUT = R.layout.row_certificates_child;
        
        private final TextView nameText;       
        private final TextView descriptionText;
        private final ImageView levelImage;
        
        private TreeChildHolder(final View view) {
            this.nameText = (TextView)view.findViewById(R.id.certificateNameText);
            this.descriptionText = (TextView)view.findViewById(R.id.certificateDescriptionText);
            this.levelImage = (ImageView)view.findViewById(R.id.certificateImage);
        }
        
        private void render(final Certificate certificate) {
            this.nameText.setText(certificate.getName());
            
            String displayed = StringUtils.removeStart(certificate.getDescription(), "This certificate represents a level of ");
            displayed = StringUtils.removeStart(displayed, "This certificate represents level of ");//mind the "a"...
            displayed = StringUtils.substringBefore(displayed, ".") + ".";
            displayed = StringUtils.capitalize(displayed);
            this.descriptionText.setText(displayed);
            this.levelImage.setVisibility(View.GONE);
        }
        
        //I18N
        private void render(final Certificate certificate, final Level characterType) {
            this.nameText.setText(certificate.getName());
            if (null == characterType) {
                this.levelImage.setVisibility(View.GONE);
                this.descriptionText.setText("");                               
                return;
            }
                        
            this.levelImage.setVisibility(View.VISIBLE);
            switch (characterType) {
                case BASIC:
                    this.descriptionText.setText("Basic");                  
                    this.levelImage.setImageLevel(1);
                    break;
                case STANDARD:
                    this.descriptionText.setText("Standard");
                    this.levelImage.setImageLevel(2);
                    break;
                case IMPROVED:
                    this.descriptionText.setText("Improved");
                    this.levelImage.setImageLevel(3);
                    break;
                case ADVANCED:
                    this.descriptionText.setText("Advanced");
                    this.levelImage.setImageLevel(4);
                    break;
                case ELITE:
                    this.descriptionText.setText("Elite");
                    this.levelImage.setImageLevel(5);
                    break;
                default:
                    this.descriptionText.setText("Impossible");                    
                    this.levelImage.setImageLevel(0);
                    break;
            }            
        }
        
        public static View create(final Context context) {
            View view = LayoutInflater.from(context).inflate(LAYOUT, null);
            view.setTag(new TreeChildHolder(view));
            return view;
        }
        
        public static void render(final View view, final Certificate certificate) {
            TreeChildHolder h = (TreeChildHolder)view.getTag();
            h.render(certificate);
        }
        
        public static void render(final View view, final Certificate certificate, final Level characterType) {
            TreeChildHolder h = (TreeChildHolder)view.getTag();
            h.render(certificate, characterType);
        }
    }
    
    private final CertificateTree tree;
  //  private final Map<Long, String> skillGroupLabels;
    
//    private final List<Long> skillGroups;
    
    private final Map<Certificate, Level> characterCertificates;
    private final Map<Long, Level> characterGroupCertificates;
    
    public CertificateTreeAdapter(final CertificateTree tree) {
        super();
        this.tree = tree;
     //   this.skillGroups = new ArrayList<>();
       // this.skillGroups.addAll(skillGroups.keySet());
     //   this.skillGroupLabels = skillGroups;
        this.characterCertificates = new HashMap<>();
        this.characterGroupCertificates = new HashMap<>();
    }
    
    public final void setCharacter(final EveCharacter character) {
        this.characterCertificates.clear();
        this.characterGroupCertificates.clear();
        if (null != character) {
	        for (Long group: this.tree.getCertificateGroups()) {
	            Level groupLevel = null;
	            boolean first = true;
	            
	            for (Certificate c: this.tree.getCertificates(group)) {
	                final Level level = getCertificateLevel(character, c);
	                this.characterCertificates.put(c, level);
	                if (first) {
	                    first = false;
	                    groupLevel = level;
	                }
	                else {
	                    groupLevel = Level.min(groupLevel, level);
	                }
	            }    	            
	            this.characterGroupCertificates.put(group, groupLevel);
	        }
        }
        notifyDataSetChanged();	        
    }
    
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        final List<Certificate> certificates = this.tree.getCertificates(getGroupId(groupPosition));
        return certificates.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        final Certificate c = (Certificate)getChild(groupPosition, childPosition);
        return c.getCertificateID();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = TreeGroupHolder.create(parent.getContext());
        }
       /* final Long groupId = getGroupId(groupPosition);
        String name = this.skillGroupLabels.get(groupId);
        name = (StringUtils.isBlank(name) ? "" + groupId : name.trim());
        if (this.characterGroupCertificates.isEmpty()) {
            TreeGroupHolder.render(view, name);
        }
        else {
            TreeGroupHolder.render(view, name, this.characterGroupCertificates.get(groupId));
        }        */
        return view;
    }

    @Override
    public View getChildView(int groupPosition,
                             int childPosition,
                             boolean isLastChild,
                             View convertView,
                             ViewGroup parent) {
        
        View view = convertView;
        if (null == view) {
            view = TreeChildHolder.create(parent.getContext());
        }
        final Certificate certificate = (Certificate)getChild(groupPosition, childPosition);
        if (this.characterCertificates.isEmpty()) {
            TreeChildHolder.render(view, certificate);
        }
        else {
            TreeChildHolder.render(view, certificate, this.characterCertificates.get(certificate));
        }
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        final List<Certificate> certificates = this.tree.getCertificates(getGroupId(groupPosition));
        return (null == certificates) ? 0 : certificates.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.tree.getCertificates(getGroupId(groupPosition));
    }

    @Override
    public int getGroupCount() {
        //return this.skillGroups.size();
        return 0;
    }

    @Override
    public long getGroupId(int groupPosition) {        
        //return this.skillGroups.get(groupPosition);
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }	    
    
    private static Level getCertificateLevel(final EveCharacter character, final Certificate certificate) {
        final Map<Long, Map<Level, Integer>> required = certificate.getRequiredSkills();
        Level type = Level.ELITE;
        
        for (Long skillID: required.keySet()) {
            final int skillLevel = character.getTraining().getSkillLevel(skillID);
            final Level highest = getCertificateSkillLevel(certificate, skillID, skillLevel);
            if (null == highest) {
                type = null;
            }
            else {
                type = Level.min(type, highest);
            }
            if (null == type) {
                break;
            }
        }
        return type;
    }

    private static Level getCertificateSkillLevel(final Certificate certificate, final long skillID, final int skillLevel) {
        final Map<Level, Integer> levels = certificate.getRequiredSkills().get(skillID);
        if (skillLevel >= levels.get(Level.ELITE)) {
            return Level.ELITE;
        }
        if (skillLevel >= levels.get(Level.ADVANCED)) {
            return Level.ADVANCED;
        }
        if (skillLevel >= levels.get(Level.IMPROVED)) {
            return Level.IMPROVED;
        }
        if (skillLevel >= levels.get(Level.STANDARD)) {
            return Level.STANDARD;
        }
        if (skillLevel >= levels.get(Level.BASIC)) {
            return Level.BASIC;
        }
        return null;
    }
}