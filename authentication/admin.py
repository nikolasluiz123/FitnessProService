from django.contrib import admin
from django.contrib.admin import ModelAdmin

from authentication.models import *


# Register your models here.
class AcademyAdmin(ModelAdmin):
    list_display = ('id', 'name')
    list_display_links = ('id', 'name')
    search_fields = ('name',)
    list_per_page = 20


admin.site.register(Academy, AcademyAdmin)
