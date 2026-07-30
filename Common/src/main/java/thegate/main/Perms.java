/*
 * Decompiled with CFR 0.152.
 */
package thegate.main;

public enum Perms {
    thegate_admin_abydoscartouche,
    thegate_admin_syncdatabase,
    thegate_admin_dialgate,
    thegate_admin_creategate,
    thegate_admin_editgate,
    thegate_admin_repairgate,
    thegate_admin_removegate,
    thegate_admin_editallgates,
    thegate_admin_reloadconfig,
    thegate_admin_lockgate,
    thegate_admin_setprivate,
    thegate_admin_tptogate,
    thegate_admin_editnetwork,
    thegate_admin_editowner,
    thegate_admin_editcoowner,
    thegate_admin_editmaterial,
    thegate_admin_breakdhd,
    thegate_admin_ignoreiris,
    thegate_admin_useiris,
    thegate_admin_editidccode,
    thegate_admin_editirismaterial,
    thegate_admin_gatedescriptioncommand,
    thegate_admin_gatenamecommand,
    thegate_admin_gatecreatecommand,
    thegate_admin_gaterotatecommand,
    thegate_admin_gaterotatenearcommand,
    thegate_admin_editgatecommands,
    thegate_admin_editgateuseperms,
    thegate_owner_editgate,
    thegate_owner_creategate,
    thegate_owner_repairgate,
    thegate_owner_removegate,
    thegate_owner_editnetwork,
    thegate_owner_lockgate,
    thegate_owner_setprivate,
    thegate_owner_tptogate,
    thegate_owner_editmaterial,
    thegate_owner_editirismaterial,
    thegate_owner_editidccode,
    thegate_owner_useiris,
    thegate_owner_gatedescriptioncommand,
    thegate_owner_gatenamecommand,
    thegate_owner_gatecreatecommand,
    thegate_owner_gaterotatecommand,
    thegate_owner_gaterotatenearcommand,
    thegate_owner_editgateuseperms,
    thegate_user_dialgate,
    thegate_user_canceldialing,
    thegate_user_quickdial,
    thegate_user_allowteleport,
    thegate_user_abydoscartouche,
    thegate_user_visablenetwork_,
    thegate_user_gatetools,
    thegate_user_idctransmitter,
    thegate_user_viewgatename,
    thegate_user_dialassist,
    thegate_user_dial_,
    thegate_user_teleport_;


    public String value() {
        return this.name().replace("_", ".");
    }
}

