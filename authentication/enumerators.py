from enum import Enum


class EnumUserProfile(Enum):
    STUDENT = "STUDENT"
    TRAINER = "TRAINER"
    NUTRITIONIST = "NUTRITIONIST"

    @classmethod
    def choices(cls):
        return [(key.name, key.value) for key in cls]


class EnumUserGroups(Enum):
    STUDENT_PERMISSIONS = "STUDENT_PERMISSIONS"
    SUPER_USER_PERMISSIONS = "SUPER_USER_PERMISSIONS"
    TRAINER_PERMISSIONS = "TRAINER_PERMISSIONS"
    NUTRITIONIST_PERMISSIONS = "NUTRITIONIST_PERMISSIONS"
