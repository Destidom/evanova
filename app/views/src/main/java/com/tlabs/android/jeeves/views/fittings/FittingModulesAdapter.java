package com.tlabs.android.jeeves.views.fittings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.SelectableExpandableRecyclerAdapter;
import com.tlabs.eve.api.ItemAttribute;
import com.tlabs.eve.dogma.Fitter;
import com.tlabs.eve.dogma.extra.format.AttributeFormat;
import com.tlabs.eve.dogma.model.Attribute;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FittingModulesAdapter extends SelectableExpandableRecyclerAdapter<FittingModulesAdapter.SlotHolder, FittingModulesAdapter.ModuleHolder> {

    static class Slot implements ParentListItem {

        private final List<Fitter.Module> modules = new ArrayList<>();
        private final int attrID;

        public Slot(int attrID) {
            this.attrID = attrID;
        }

        @Override
        public List<?> getChildItemList() {
            return this.modules;
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }


    }

    static class SlotHolder extends ParentViewHolder {

        TextView textView;

        public SlotHolder(View view) {
            super(view);
            textView = (TextView)view;
        }

        public void render(final Slot slot) {
            int totalCount = slot.modules.size();

            int filledCount =
                    CollectionUtils.select(
                    slot.modules,
                    input -> ((Fitter.Module)input).getItem() != null)
                    .size();

            switch (slot.attrID) {
                case Attribute.FIT_HIGH_SLOTS:
                    setText(R.string.jeeves_fit_slots_high, totalCount, filledCount);
                    break;
                case Attribute.FIT_MEDIUM_SLOTS:
                    setText(R.string.jeeves_fit_slots_medium, totalCount, filledCount);
                    break;
                case Attribute.FIT_LOW_SLOTS:
                    setText(R.string.jeeves_fit_slots_low, totalCount, filledCount);
                    break;
                case Attribute.FIT_RIGS_SLOTS:
                    setText(R.string.jeeves_fit_slots_rigs, totalCount, filledCount);
                    break;
                case Attribute.FIT_SUBSYSTEM_SLOTS:
                    setText(R.string.jeeves_fit_slots_subsystems, totalCount, filledCount);
                    break;
                default:
                    textView.setText("" + slot.attrID);
                    break;
            }
        }

        private void setText(final int rid, final Object... args) {
            textView.setText(textView.getResources().getString(rid, args));
        }
    }

    static class ModuleHolder extends ChildViewHolder {

      //  @BindView(R.id.fittingItemName)
        TextView nameView;

      //  @BindView(R.id.fittingItemCategoryIcon)
        ImageView iconView;

     //   @BindView(R.id.fittingItemGridText)
        TextView gridText;

     //   @BindView(R.id.fittingItemCPUText)
        TextView cpuText;

     //   @BindView(R.id.fittingItemGridIcon)
        ImageView gridIcon;

      //  @BindView(R.id.fittingItemCPUIcon)
        ImageView cpuIcon;

        public ModuleHolder(final View view) {
            super(view);
        }

        public void render(final Fitter.Module module) {
            if (null == module.getItem()) {
                renderEmpty(module);
                return;
            }
            EveImages.loadItemIcon(module.getItem().getItemID(), iconView);
            nameView.setText(module.getItem().getItemName());

            cpuIcon.setVisibility(View.VISIBLE);
            cpuText.setVisibility(View.VISIBLE);
            gridIcon.setVisibility(View.VISIBLE);
            gridText.setVisibility(View.VISIBLE);

            cpuText.setText(AttributeFormat.format(module.getItem().getAttribute(ItemAttribute.CPU_NEED)));
            gridText.setText(AttributeFormat.format(module.getItem().getAttribute(ItemAttribute.POWER_NEED)));

        }

        private void renderEmpty(final Fitter.Module module) {
            nameView.setText("Empty");

            cpuIcon.setVisibility(View.INVISIBLE);
            cpuText.setVisibility(View.INVISIBLE);
            gridIcon.setVisibility(View.INVISIBLE);
            gridText.setVisibility(View.INVISIBLE);

            switch (module.getSlotID()) {
                case ItemAttribute.FIT_HIGH_SLOTS :
                    iconView.setImageResource(R.drawable.icon_slot_high);
                    break;
                case ItemAttribute.FIT_MEDIUM_SLOTS :
                    iconView.setImageResource(R.drawable.icon_slot_medium);
                    break;
                case ItemAttribute.FIT_LOW_SLOTS :
                    iconView.setImageResource(R.drawable.icon_slot_low);
                    break;
                case ItemAttribute.FIT_RIGS_SLOTS :
                    iconView.setImageResource(R.drawable.icon_slot_rigs);
                    break;
                case ItemAttribute.FIT_SUBSYSTEM_SLOTS :
                    iconView.setImageResource(R.drawable.icon_slot_subsystems);
                    break;
                default:
                    break;
            }
        }
    }

    private final Fitter fitter;

    public FittingModulesAdapter(Fitter fitter) {
        super(build(fitter));
        this.fitter = fitter;
    }

    @Override
    public final SlotHolder onCreateParentViewHolder(ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_fitting_slot, parent, false);
        return new SlotHolder(view);
    }

    @Override
    public final ModuleHolder onCreateChildViewHolder(ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_fitting_item, parent, false);
        return new ModuleHolder(view);
    }

    @Override
    public void onBindParentViewHolder(SlotHolder slotHolder, int position, ParentListItem parent) {
        slotHolder.render((Slot) parent);
    }

    @Override
    public void onBindChildViewHolder(ModuleHolder moduleHolder, int i, Object child, boolean selected, boolean loading) {
        moduleHolder.render((Fitter.Module) child);
    }

    public boolean getFull() {
        return this.fitter.isFull();
    }

    public final List<Fitter.Module> getEmptySelectedModules() {
        final List<Fitter.Module> modules = getSelectedModules();
        CollectionUtils.filter(modules, o -> (null == ((Fitter.Module)o).getItem()));
        return modules;
    }

    public final List<Fitter.Module> getFilledSelectedModules() {
        final List<Fitter.Module> modules = getSelectedModules();
        CollectionUtils.filter(modules, o -> (null != ((Fitter.Module)o).getItem()));
        return modules;
    }

    public final List<Fitter.Module> getSelectedModules() {
        final List<Object> selected = getSelected();
        final List<Fitter.Module> modules = new ArrayList<>(selected.size());
        for (Object o: selected) {
            final Fitter.Module m = (Fitter.Module)o;
            modules.add(m);
        }
        return modules;
    }

    public Fitter.Module addModule(final long item) {
        return fitter.fit(item);
    }

    public List<Fitter.Module> removeSelected() {
        final List<Fitter.Module> modules = new ArrayList<>();
        for (Fitter.Module m: getSelectedModules()) {
            if (fitter.unfit(m)) {
                modules.add(m);
            }
        }
        return modules;
    }

    protected boolean onModuleClicked(Fitter.Module module) {
        return true;
    }

    protected boolean onModuleSelected(Fitter.Module module, boolean selected) {
        return true;
    }

    @Override
    protected final boolean onItemClicked(Object module) {
        return onModuleClicked((Fitter.Module) module);
    }

    @Override
    protected final boolean onItemSelected(Object module, boolean selected) {
        return onModuleSelected((Fitter.Module)module, selected);
    }

    private static List<Slot> build(final Fitter fitter) {
        final Map<Integer, Slot> slots = new HashMap<>();
        for (Fitter.Module m: fitter.getModules().values()) {
            Slot slot = slots.get(m.getSlotID());
            if (null == slot) {
                slot = new Slot(m.getSlotID());
                slots.put(m.getSlotID(), slot);
            }
            slot.modules.add(m);
        }
        final List<Slot> r = new ArrayList<>();
        r.addAll(slots.values());
        return r;
    }

}
