package com.tlabs.android.jeeves.views.contracts;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.eve.api.Contract;

import org.apache.commons.lang.StringUtils;

final class ContractListHolder extends AbstractListRowHolder<Contract> {

    private static final long H24 = 24L * 60 * 60 * 1000;
	
	private TextView contractItem;
    private TextView contractStatus;
    private TextView contractType;
    private TextView contractPrice;
    private TextView contractDate;
	
	public ContractListHolder(View view) {
		super(view);
		this.contractItem = (TextView)view.findViewById(R.id.j_row_contractItem);
		this.contractStatus = (TextView)view.findViewById(R.id.j_row_contractStatus);
		this.contractType = (TextView)view.findViewById(R.id.j_row_contractType);
		this.contractPrice = (TextView)view.findViewById(R.id.j_row_contractPrice);
		this.contractDate = (TextView)view.findViewById(R.id.j_row_contractDate);
	}

    @Override
	public void render(final Contract c) {
		String title = c.getTitle();
		if (StringUtils.isBlank(title)) {
			contractItem.setText(R.string.jeeves_contract_title_untitled);
		}
		else {
			contractItem.setText(title);
		}
		
		contractPrice.setText(contractItem.getResources().getString(R.string.jeeves_contract_price_text,
                EveFormat.Currency.MEDIUM(c.getPrice(), false),
                EveFormat.Currency.MEDIUM(c.getReward(), false)));
		
		renderType(c);
		renderStatus(c);		
	}
	
	private void renderStatus(final Contract c) {                    
	    final Resources r = contractStatus.getResources();
	    switch (c.getStatus()) {
	        case IN_PROGRESS: {
	            contractItem.setTextColor(Color.WHITE);
	            
	            final long dueDate = c.getDateAccepted() + c.getNumDays() * H24;
	            contractDate.setText(r.getString(R.string.jeeves_contract_status_due_on, EveFormat.DateTime.SHORT(dueDate)));
	            final long remaining = dueDate - System.currentTimeMillis();
	            if (remaining < 0) {
	                contractDate.setTextColor(Color.RED);       
	                contractStatus.setText(R.string.jeeves_contract_status_overdue);
	                contractStatus.setTextColor(Color.WHITE);
	            }
	            else {
	                contractDate.setTextColor(EveFormat.getDurationColor(remaining));
	                contractStatus.setText(R.string.jeeves_contract_status_inprogress);
	                contractStatus.setTextColor(Color.WHITE);
	            }
	            break;
	        }
	        case OUTSTANDING: {
	            final int orange = r.getColor(R.color.orange);
                contractItem.setTextColor(orange);
	            contractStatus.setText(R.string.jeeves_contract_status_outstanding);
	            contractStatus.setTextColor(orange);	   
	            
	            final long remaining = c.getDateExpired() - System.currentTimeMillis();
                if (remaining < 0) {
                    contractDate.setText(R.string.jeeves_contract_status_expired);
                    contractDate.setTextColor(Color.RED);       
                }
                else {
                    contractDate.setText(r.getString(R.string.jeeves_contract_status_expires_on, EveFormat.DateTime.SHORT(c.getDateExpired())));
                    contractDate.setTextColor(EveFormat.getDurationColor(remaining));
                }
                break;
	        }
	        case DELETED: {
	            contractItem.setTextColor(Color.GRAY);
	            contractStatus.setText(R.string.jeeves_contract_status_deleted);
	            contractStatus.setTextColor(Color.GRAY);
	            
	            contractDate.setText("");
	            contractDate.setTextColor(Color.GRAY);
                break;
	        }
	        case COMPLETED_COMPLETED:
	        case COMPLETED_CONTRACTOR:
	        case COMPLETED_ISSUER: {
	            contractItem.setTextColor(Color.GRAY);
	            contractStatus.setText(R.string.jeeves_contract_status_completed);
	            contractStatus.setTextColor(Color.GRAY);
	            
	            contractDate.setText(r.getString(R.string.jeeves_contract_status_completed_on, EveFormat.DateTime.SHORT(c.getDateCompleted())));
	            contractDate.setTextColor(Color.GRAY);
                break;
	        }
	        case FAILED: {
	            contractItem.setTextColor(Color.GRAY);
	            contractStatus.setText(R.string.jeeves_contract_status_failed);
	            contractStatus.setTextColor(Color.RED);
	            
                final long dueDate = c.getDateAccepted() + c.getNumDays() * H24;
                contractDate.setText(r.getString(R.string.jeeves_contract_status_due_on, EveFormat.DateTime.SHORT(dueDate)));
	            contractDate.setTextColor(Color.GRAY);
                break;
	        }
	        case CANCELLED: {
	            contractItem.setTextColor(Color.GRAY);
	            contractStatus.setText(R.string.jeeves_contract_status_cancelled);
	            contractStatus.setTextColor(Color.GRAY);
	            
	            contractDate.setText("");
	            contractDate.setTextColor(Color.GRAY);
                break;
	        }
	        case REJECTED: {
	            contractItem.setTextColor(Color.GRAY);
	            contractStatus.setText(R.string.jeeves_contract_status_rejected);
	            contractStatus.setTextColor(Color.GRAY);
	            
	            contractDate.setText("");
	            contractDate.setTextColor(Color.GRAY);
                break;
	        }
	        case REVERSED: {
	            contractItem.setTextColor(Color.GRAY);
	            contractStatus.setText(R.string.jeeves_contract_status_reversed);
	            contractStatus.setTextColor(Color.GRAY);
	            
	            contractDate.setText("");
	            contractDate.setTextColor(Color.GRAY);	            
                break;
	        }
	        case UNKNOWN: 
	        default: {
	            contractItem.setTextColor(Color.GRAY);
	            contractStatus.setText(R.string.jeeves_unknown);
	            contractStatus.setTextColor(Color.GRAY);
	            
	            contractDate.setText("");
	            contractDate.setTextColor(Color.GRAY);	            	            
                break;
	        }
	    }
	}
    
    private void renderType(final Contract c) {                  
        switch (c.getType()) {
            case AUCTION: 
                contractType.setText(R.string.jeeves_contract_type_auction);
                break;
            case EXCHANGE:
                contractType.setText(R.string.jeeves_contract_type_exchange);
                break;
            case COURIER:
                contractType.setText(R.string.jeeves_contract_type_courier);
                break;
            case UNKNOWN:
            default:
                contractType.setText(R.string.jeeves_unknown);
                break;
        }                       
    }
}