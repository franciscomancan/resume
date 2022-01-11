package com.cs.stadosweb.enumtype;

/** At creation, here only for a potential refactor,
 * 	in the case that I can get the database to represent
 * 	this ordinally, rather than varchar(255), as with
 * 	the first implementation <code>InvoiceProductOverride</code>.
 */
public enum InvoiceStatus {
	STATUS_CREATED,
	STATUS_OPEN,
    STATUS_SERVED,
    STATUS_CLOSED,
    STATUS_PAYED
}
