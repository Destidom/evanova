package com.tlabs.android.jeeves.views.contracts;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.api.Contract;

import org.apache.commons.lang3.StringUtils;

final class ContractDetailsHolder {
	
    private static final long H24 = 24L * 60 * 60 * 1000;
    
	private final TextView issuerText;
	private final TextView contractorText;		
	private final TextView availabilityText;
	private final TextView locationText;		
	private final TextView dateIssuedText;
	
	private final TextView dateExpiredLabel;//expiration date | Date completed
	private final TextView dateExpiredText;//expiration date | Date completed
	
	private final TextView typeText;
	
	private final TableRow priceRow;
	private final TextView priceText;
	
	private final TableRow rewardRow;
	private final TextView rewardText;
	
	private final TableRow buyOutRow;
	private final TextView buyOutText;
	
	private final TableRow collateralRow;
	private final TextView collateralText;
	
	private final TableRow volumeRow;
	private final TextView volumeText;
	
	private final TableRow destinationRow;
	private final TextView destinationText;
	
	private final ViewGroup titleView;
	

	public ContractDetailsHolder(View mainView) {
		this.issuerText = (TextView)mainView.findViewById(R.id.j_contract_IssuedBy);
		this.contractorText = (TextView)mainView.findViewById(R.id.j_contract_IssuedTo);
		this.availabilityText = (TextView)mainView.findViewById(R.id.j_contract_Availability);
		this.locationText = (TextView)mainView.findViewById(R.id.j_contract_Location);
		this.dateIssuedText = (TextView)mainView.findViewById(R.id.j_contract_DateIssued);
		
		this.dateExpiredLabel = (TextView)mainView.findViewById(R.id.j_contract_DateExpirationLabel);
		this.dateExpiredText = (TextView)mainView.findViewById(R.id.j_contract_DateExpiration);
		
		this.typeText = (TextView)mainView.findViewById(R.id.j_contract_TypeText);
		
		this.priceRow = (TableRow)mainView.findViewById(R.id.j_contract_PriceRow);
		this.priceText = (TextView)mainView.findViewById(R.id.j_contract_Price);
		
		this.rewardRow = (TableRow)mainView.findViewById(R.id.j_contract_RewardRow);
		this.rewardText = (TextView)mainView.findViewById(R.id.j_contract_Reward);
		
		this.buyOutRow = (TableRow)mainView.findViewById(R.id.j_contract_BuyoutRow);
		this.buyOutText = (TextView)mainView.findViewById(R.id.j_contract_Buyout);
		
		this.collateralRow = (TableRow)mainView.findViewById(R.id.j_contract_CollateralRow);
		this.collateralText = (TextView)mainView.findViewById(R.id.j_contract_Collateral);
		
		this.volumeRow = (TableRow)mainView.findViewById(R.id.j_contract_VolumeRow);
		this.volumeText = (TextView)mainView.findViewById(R.id.j_contract_Volume);
		
		this.destinationRow = (TableRow)mainView.findViewById(R.id.j_contract_DestinationRow);
		this.destinationText = (TextView)mainView.findViewById(R.id.j_contract_Destination);
		
		this.titleView = (ViewGroup)mainView.findViewById(R.id.j_contract_TitleGroup);

	}
	
	final void setTitleVisible(boolean visible) {
		this.titleView.setVisibility(visible ? View.VISIBLE : View.GONE);
	}
	
	public final void render(final Contract c, final long ownerID) {
		this.issuerText.setText(renderIssuer(c));
		this.contractorText.setText(renderContractor(c));
		this.availabilityText.setText(c.getAvailability());
		this.locationText.setText(renderLocation(c));
		this.dateIssuedText.setText(EveFormat.DateTime.SHORT(c.getDateIssued()));
		this.volumeText.setText("" + c.getVolume() + " m3");
		
		final TextView subtitleText = (TextView)this.titleView.findViewById(R.id.j_contract_TitleSubText);
		String title = c.getTitle();
		if (StringUtils.isBlank(title)) {
			subtitleText.setText(R.string.jeeves_contract_title_untitled);
		}
		else {
			subtitleText.setText(title);
		}
		renderStatus(c);
		renderType(c);
		renderOwner(c, c.getIssuerID() == ownerID);
	}	
	
	private void renderStatus(final Contract c) {
	    final TextView descriptionText = (TextView)this.titleView.findViewById(R.id.j_contract_TitleDescription);
	    
	    switch (c.getStatus()) {
	        case IN_PROGRESS: {
	            this.dateExpiredLabel.setText(R.string.jeeves_contract_date_label_due_on);
	            final long dueDate = c.getDateAccepted() + c.getNumDays() * H24;
                dateExpiredText.setText(EveFormat.DateTime.SHORT(dueDate));
	            
                final long remaining = dueDate - System.currentTimeMillis();
                if (remaining < 0) {
                    dateExpiredText.setTextColor(Color.RED);       
                    descriptionText.setText(R.string.jeeves_contract_status_overdue);
                    descriptionText.setTextColor(Color.WHITE);
                }
                else {
                    dateExpiredText.setTextColor(EveFormat.getDurationColor(remaining));
                    descriptionText.setText(R.string.jeeves_contract_status_inprogress);
                    descriptionText.setTextColor(Color.WHITE);
                }
                break;
            }
            case OUTSTANDING: {
                this.dateExpiredLabel.setText(R.string.jeeves_contract_date_label_expires_on);
               
                final long remaining = c.getDateExpired() - System.currentTimeMillis();
                if (remaining < 0) {
                    dateExpiredText.setText(R.string.jeeves_contract_status_expired);
                    dateExpiredText.setTextColor(Color.RED);       
                }
                else {
                    dateExpiredText.setText(EveFormat.DateTime.SHORT(c.getDateExpired()));
                    dateExpiredText.setTextColor(EveFormat.getDurationColor(remaining));
                }
                descriptionText.setText(R.string.jeeves_contract_status_outstanding);
                descriptionText.setTextColor(descriptionText.getResources().getColor(R.color.orange));
                
                break;
            }
            case DELETED: {
                this.dateExpiredLabel.setText(R.string.jeeves_contract_date_label_status);
                this.dateExpiredText.setText(R.string.jeeves_contract_status_deleted);
                this.dateExpiredText.setTextColor(Color.LTGRAY);
                
                descriptionText.setText(R.string.jeeves_contract_status_deleted);
                descriptionText.setTextColor(Color.GRAY);
                break;
            }
            case COMPLETED_COMPLETED:
            case COMPLETED_CONTRACTOR:
            case COMPLETED_ISSUER: {
                this.dateExpiredLabel.setText(R.string.jeeves_contract_date_label_completed_on);
                this.dateExpiredText.setText(EveFormat.DateTime.SHORT(c.getDateCompleted()));
                this.dateExpiredText.setTextColor(Color.LTGRAY);
                
                descriptionText.setText(R.string.jeeves_contract_status_completed);
                descriptionText.setTextColor(Color.GRAY);
                break;
            }
            case FAILED: {
                this.dateExpiredLabel.setText(R.string.jeeves_contract_date_label_due_on);
                final long dueDate = c.getDateAccepted() + c.getNumDays() * H24;
                this.dateExpiredText.setText(EveFormat.DateTime.SHORT(dueDate));
                this.dateExpiredText.setTextColor(Color.LTGRAY);
                
                descriptionText.setText(R.string.jeeves_contract_status_failed);
                descriptionText.setTextColor(Color.RED);
                break;
            }
            case CANCELLED: {
                this.dateExpiredLabel.setText(R.string.jeeves_contract_date_label_status);
                this.dateExpiredText.setText(R.string.jeeves_contract_status_cancelled);
                this.dateExpiredText.setTextColor(Color.LTGRAY);
                
                descriptionText.setText(R.string.jeeves_contract_status_cancelled);
                descriptionText.setTextColor(Color.GRAY);
                break;
            }
            case REJECTED: {               
                this.dateExpiredLabel.setText(R.string.jeeves_contract_date_label_status);
                this.dateExpiredText.setText(R.string.jeeves_contract_status_rejected);
                this.dateExpiredText.setTextColor(Color.LTGRAY);
                
                descriptionText.setText(R.string.jeeves_contract_status_rejected);
                descriptionText.setTextColor(Color.GRAY);
                break;
            }
            case REVERSED: {
                this.dateExpiredLabel.setText(R.string.jeeves_contract_date_label_status);
                this.dateExpiredText.setText(R.string.jeeves_contract_status_reversed);
                this.dateExpiredText.setTextColor(Color.LTGRAY);
                
                descriptionText.setText(R.string.jeeves_contract_status_reversed);
                descriptionText.setTextColor(Color.GRAY);
                break;
            }
            case UNKNOWN: 
                this.dateExpiredLabel.setText(R.string.jeeves_contract_date_label_status);
                this.dateExpiredText.setText(R.string.jeeves_unknown);
                this.dateExpiredText.setTextColor(Color.LTGRAY);
                
                descriptionText.setText(R.string.jeeves_unknown);
                descriptionText.setTextColor(Color.GRAY);
            default: {
                break;
            }
	    }
	}
	
	private void renderType(final Contract c) {        
        switch (c.getType()) {
            case AUCTION: 
                this.typeText.setText(R.string.jeeves_contract_type_auction);
                this.collateralRow.setVisibility(View.GONE);
                this.buyOutRow.setVisibility(View.VISIBLE);                         
                this.buyOutText.setText(EveFormat.Currency.LONG(c.getBuyout()));
                this.destinationRow.setVisibility(View.GONE);
                break;
            case EXCHANGE:
                this.typeText.setText(typeText.getResources().
                        getString(R.string.jeeves_contract_type_exchange, c.getAvailability()));
                this.collateralRow.setVisibility(View.GONE);
                this.buyOutRow.setVisibility(View.GONE);
                this.destinationRow.setVisibility(View.GONE);
                this.destinationRow.setVisibility(View.VISIBLE);
                this.destinationText.setText(c.getEndStationName());                    
                break;
            case COURIER:
                this.typeText.setText(typeText.getResources().
                        getString(R.string.jeeves_contract_type_courier, c.getAvailability()));
                this.collateralRow.setVisibility(View.VISIBLE);
                this.collateralText.setText(EveFormat.Currency.LONG(c.getCollateral()));
                this.buyOutRow.setVisibility(View.GONE);
                this.destinationRow.setVisibility(View.VISIBLE);
                this.destinationText.setText(c.getEndStationName());
                break;
            case UNKNOWN:
            default:                    
                this.typeText.setText(c.getType().getTypeName());
                this.destinationRow.setVisibility(View.GONE);
                this.collateralRow.setVisibility(View.GONE);
                this.buyOutRow.setVisibility(View.GONE);
                break;
        }                   
    }
	
	private void renderOwner(final Contract c, final boolean isOwner) {        
        if (isOwner) {
            if (c.getReward() == 0) {                           
                this.priceRow.setVisibility(View.GONE);             
            }
            else {
                this.priceRow.setVisibility(View.VISIBLE);
                this.priceText.setText(EveFormat.Currency.LONG(c.getReward()));
            }
            if (c.getPrice() == 0) {                
                this.rewardRow.setVisibility(View.GONE);                
            }
            else {
                this.rewardRow.setVisibility(View.VISIBLE);
                this.rewardText.setText(EveFormat.Currency.LONG(c.getPrice()));
            }   
        }
        else {
            if (c.getReward() == 0) {                           
                this.rewardRow.setVisibility(View.GONE);                
            }
            else {
                this.rewardRow.setVisibility(View.VISIBLE);
                this.rewardText.setText(EveFormat.Currency.LONG(c.getReward()));
            }
            if (c.getPrice() == 0) {                
                this.priceRow.setVisibility(View.GONE);             
            }
            else {
                this.priceRow.setVisibility(View.VISIBLE);
                this.priceText.setText(EveFormat.Currency.LONG(c.getPrice()));
            }           
        }
    }   
	
	private String renderIssuer(final Contract c) {
		if (c.getIssuerID() < 1 && c.getForCorpID() < 1) {
			return "";
		}
		String r = "";
		if (c.getIssuerID() > 0) {
			String name = c.getIssuerName();
			if (StringUtils.isBlank(name)) {
				r = r + Long.toString(c.getIssuerID());
			}
			else {
				r = r + name;
			}
		}
		if (c.getForCorpID() > 0) {
			if (c.getIssuerID() > 0) {
				r = r + this.issuerText.getResources().getString(R.string.jeeves_contract_issuer_behalf_token);
			}
			if (StringUtils.isBlank(c.getForCorpName())) {
				r = r + this.issuerText.getResources().getString(R.string.jeeves_contract_issuer_corp_token) + c.getForCorpID();
			}
			else {
				r = r + c.getIssuerCorpName();
			}
		}
		return r;
	}
	
	private String renderContractor(final Contract c) {
		if (c.getAcceptorID() > 0) {
			return StringUtils.isBlank(c.getAcceptorName()) ?
					"" + c.getAcceptorID() : c.getAcceptorName();
		}
		if (c.getAssigneeID() > 0) {				
			return StringUtils.isBlank(c.getAssigneeName()) ?
					"" + c.getAssigneeID() : c.getAssigneeName();
		}
		return this.contractorText.getResources().getString(R.string.jeeves_contract_contractor_unassigned);
	}
	
	private static String renderLocation(final Contract c) {
		if (StringUtils.isBlank(c.getStartStationName())) {
			return Long.toString(c.getStartStationID());
		}
		return c.getStartStationName();
	}				
}