from enum import Enum


class EnumUserProfile(Enum):
    STUDENT = "STUDENT"
    TRAINER = "TRAINER"
    NUTRITIONIST = "NUTRITIONIST"

    @classmethod
    def choices(cls):
        return [(key.name, key.value) for key in cls]
