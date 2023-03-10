package org.nekotori.type;


import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.List;
import java.util.function.Predicate;

public class QQIdentityType {

    public static final Predicate<GroupMessageEvent> GROUP_ADMIN =
            groupIdentitySelector(MemberPermission.ADMINISTRATOR,MemberPermission.OWNER);

    public static final Predicate<GroupMessageEvent> GROUP_NORMAL =
            groupIdentitySelector(MemberPermission.MEMBER);


    public static Predicate<GroupMessageEvent> groupIdentitySelector(MemberPermission... permissions){
        return event -> List.of(permissions).contains(event.getSender().getPermission());
    }


}
