package com.valentin.reservacion_citas.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WebhookPayPalResourcePayloadReqDto {

	@JsonProperty("supplementary_data")
	private WebhookPayPalRelatedIdsPayloadReqDto supplementaryData;

	@JsonProperty("update_time")
	private String updateTime;

	@JsonProperty("create_time")
	private String createTime;

	@JsonProperty("invoice_id")
	private String invoiceId;

	@JsonProperty("links")
	private List<Object> links;

	@JsonProperty("id")
	private String id;

	@JsonProperty("status")
	private String status;

	public static class WebhookPayPalRelatedIdsPayloadReqDto {
		@JsonProperty("related_ids")
		private WebhookPayPalOrderPayloadReqDto relatedIds;

		public WebhookPayPalOrderPayloadReqDto getRelatedIds() {
			return relatedIds;
		}

		public void setRelatedIds(WebhookPayPalOrderPayloadReqDto relatedIds) {
			this.relatedIds = relatedIds;
		}
	}

	public static class WebhookPayPalOrderPayloadReqDto {
		@JsonProperty("order_id")
		private String orderId;

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
	}

	public WebhookPayPalRelatedIdsPayloadReqDto getSupplementaryData() {
		return supplementaryData;
	}

	public void setSupplementaryData(WebhookPayPalRelatedIdsPayloadReqDto supplementaryData) {
		this.supplementaryData = supplementaryData;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public List<Object> getLinks() {
		return links;
	}

	public void setLinks(List<Object> links) {
		this.links = links;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "WebhookPayPalResourcePayloadReqDto{" +
				"supplementaryData=" + supplementaryData +
				", updateTime='" + updateTime + '\'' +
				", createTime='" + createTime + '\'' +
				", invoiceId='" + invoiceId + '\'' +
				", links=" + links +
				", id='" + id + '\'' +
				", status='" + status + '\'' +
				'}';
	}
}
