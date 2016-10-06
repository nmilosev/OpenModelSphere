# -------------------------------------------------------------------
# Copyright (C) 2008 Grandite
#
# This file is part of Open ModelSphere.
#
# Open ModelSphere is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 3 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
# or see http://www.gnu.org/licenses/.
# 
# You can reach Grandite at: 
# 
# 20-1220 Lebourgneuf Blvd.
# Quebec, QC
# Canada  G2K 2G4
# 
# or
# 
# open-modelsphere@grandite.com
# 
# -------------------------------------------------------------------

#
# import classes
# 
from org.modelsphere.jack.srtool import ApplicationContext
from org.modelsphere.jack.baseDb.db import Db
from org.modelsphere.sms.templates import GenericMapping

#
# script's entry point
#
def main() :
	try:
		project = ApplicationContext.getFocusManager().getCurrentProject()
		db      = project.getDb()
		db.beginTrans(Db.READ_TRANS)
		metafield = GenericMapping.getMetaField("ProjectName")
		name = project.get(metafield)
		out_file = open("test.txt","w")
		out_file.write("Project name:")
		out_file.write(name)
		out_file.close() 
	finally:
		db.commitTrans()

main()
